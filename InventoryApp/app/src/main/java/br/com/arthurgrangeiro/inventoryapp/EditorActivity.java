package br.com.arthurgrangeiro.inventoryapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DecimalFormat;

import br.com.arthurgrangeiro.inventoryapp.data.ProductDbHelper;
import br.com.arthurgrangeiro.inventoryapp.data.ProductsContract.ProductEntry;

public class EditorActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_PRODUCT_LOADER = 0;

    private static final int NEW_SALE = 0;
    private static final int NEW_RECEIVE = 1;

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText mNameEditText;
    private EditText mPriceEditText;
    private EditText mQuantityEditText;
    private EditText mProviderEditText;
    private EditText mEmailEditText;
    private ImageView mImageView;

    private TextView mQuantityTextView;

    private ProductDbHelper mDbHelper;

    private Uri mCurrentUri;

    private ProductCursorAdapter mCursorAdapter;

    private boolean wichMode = true;

    private boolean mProductHasChanged = false;

    private Button buttonSaleEditor;

    private Button buttonReceiveEditor;

    Uri mCurrentPhotoPathUri;
    String mCurrentPhotoPath;

    //Verifica se o usuário interagiu com algum dos campos editáveis
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mProductHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mCurrentUri = getIntent().getData();

        Button buttonBuy = buttonBuy = (Button) findViewById(R.id.button_buy);
        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });

        LinearLayout linearLayoutQuantity = (LinearLayout) findViewById(R.id.linear_layout_quantity);

        if (mCurrentUri != null) {
            wichMode = false;
            setTitle(getResources().getString(R.string.edit_activity));
            getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
        } else {
            setTitle(getResources().getString(R.string.add_activity));
            buttonBuy.setVisibility(View.GONE);
            linearLayoutQuantity.setVisibility(View.GONE);
            invalidateOptionsMenu();
        }

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.product_name_editor);
        mPriceEditText = (EditText) findViewById(R.id.price_editor);
        mQuantityEditText = (EditText) findViewById(R.id.quantity_new_value);
        mProviderEditText = (EditText) findViewById(R.id.provider_name_editor);
        mEmailEditText = (EditText) findViewById(R.id.email_provider_editor);

        mEmailEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    saveProduct();
                    return true;
                }
                return false;
            }
        });

        //Shows the current value inside the database or the new value
        mQuantityTextView = (TextView) findViewById(R.id.quantity_editor);

        //Set mTouchListener - Verifica se o usuario interagiu com o sistema, se sim, impede que ele
        //sai sem salvar os dados.
        mNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mProviderEditText.setOnTouchListener(mTouchListener);

        mDbHelper = new ProductDbHelper(this);

        buttonSaleEditor = (Button) findViewById(R.id.button_sale_editor);
        buttonSaleEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeQuantity(NEW_SALE);
            }
        });
        buttonReceiveEditor = (Button) findViewById(R.id.button_received_editor);
        buttonReceiveEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeQuantity(NEW_RECEIVE);
            }
        });

        mImageView = (ImageView) this.findViewById(R.id.product_image);
        Button photoButton = (Button) this.findViewById(R.id.add_image_button);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //takePhoto();
                //dispatchTakePictureIntent();
                openImageSelector();
            }
        });
    }

    /**
     * Abre um aplicativo de email para que o usuário possa enviar uma mensagem.
     * O Email é recuperado do banco de dados, salvo como contato do fornecedor (provider)
     */
    private void sendEmail() {
        String email = mEmailEditText.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, R.string.no_contact, Toast.LENGTH_SHORT).show();
            return;
        }

        String[] address = {
                email
        };

        String subject = getString(R.string.new_request) + " " + mNameEditText.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, address);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.no_email), Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Verifica se o usuário está adicionando ou retirando produtos do estoque.
     * quantity tem dois valores possiveis NEW_SALE = 0 e NEW_RECEIVE = 1.
     *
     * @param quantity
     */
    private void changeQuantity(int quantity) {
        ContentValues values = new ContentValues();

        //Valor que está atualmente no Banco de Dados e que é mostrado para o usuário
        int currentQuantity = Integer.parseInt(mQuantityTextView.getText().toString());
        //Quantidade adicionada pelo usuário
        int newQuantity = 0;

        String qtda = mQuantityEditText.getText().toString();

        if (qtda.length() > 0) {
            newQuantity = Integer.parseInt(mQuantityEditText.getText().toString());
            if (newQuantity < 0) {
                newQuantity = 0;
            }
        } else {
            Toast.makeText(this, getString(R.string.write_something), Toast.LENGTH_SHORT).show();
            return;
        }

        //Valor que será salvo no banco.
        int quantityToDb;

        if (quantity == NEW_SALE) {
            quantityToDb = currentQuantity - newQuantity;
        } else {
            quantityToDb = currentQuantity + newQuantity;
        }
        if (quantityToDb >= 0) {
            values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantityToDb);
            getContentResolver().update(mCurrentUri, values, null, null);
        } else {
            Toast.makeText(this, getString(R.string.bigger_than_zero), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.action_save:
                saveProduct();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                // Se o produto não mudou, continua navegando pra cima para a activity pai
                // que é a {@link CatalogActivity}.
                if (!mProductHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                // Caso contrário se há alterações não salvas, configura um dialog para alertar o usuário.
                // Cria um click listener para lidar com o usuário confirmando que
                // mudanças devem ser descartadas.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Usuário clidou no botão "Discard", e navegou para a activity pai.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Mostra um dialog que notifica o usuário que eles tem alterações não salvas
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Salva os dados do produto no banco de dados.
     * É usado para fazer inserções e updates.
     */
    private void saveProduct() {
        // Gets the data repository in write mode
        String nameString = mNameEditText.getText().toString();
        String priceString = mPriceEditText.getText().toString();
        String quantityString = mQuantityTextView.getText().toString();
        String providerString = mProviderEditText.getText().toString();
        String emailString = mEmailEditText.getText().toString();
        String imageString = mCurrentPhotoPath;
        //String imageString = mCurrentPhotoPathUri.toString();
        if (mCurrentUri == null &&
                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(priceString) &&
                TextUtils.isEmpty(quantityString) && TextUtils.isEmpty(providerString)
                && TextUtils.isEmpty(emailString) && TextUtils.isEmpty(imageString)) {
            Toast.makeText(this, getString(R.string.empty_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(nameString)) {
            Toast.makeText(this, R.string.name_required, Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(priceString) || priceString.trim().equalsIgnoreCase(",00")) {
            Toast.makeText(this, R.string.price_required, Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(providerString)) {
            Toast.makeText(this, R.string.provider_required, Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(emailString)){
            Toast.makeText(this, R.string.email_is_required, Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(imageString)){
            Toast.makeText(this, R.string.image_is_required, Toast.LENGTH_SHORT).show();
            return;
        }

        //ContentValues - Valores a serem colocados no banco de dados
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, nameString);
        values.put(ProductEntry.COLUMN_PRODUCT_PROVIDER, providerString);
        values.put(ProductEntry.COLUMN_PRODUCT_PROVIDER_EMAIL, emailString);
        values.put(ProductEntry.COLUMN_PRODUCT_IMAGE, imageString);

        //Teste antes de colocar o preço no banco, tem que ser maior do que zero.
        double price = 0;
        if (!TextUtils.isEmpty(priceString)) {
            double teste = Double.parseDouble(priceString.replace(",", "."));
            if (teste >= 0) {
                price = teste;
            }
        }
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, price);

        //Teste antes de colocar a quantidade no banco, tem que ser maior do que zero
        int quantity = 0;
        if (!TextUtils.isEmpty(quantityString)) {
            int teste2 = Integer.parseInt(quantityString);
            if (teste2 >= 0) {
                quantity = teste2;
            }
        }
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);

        //Verifica se é update ou insert.
        if (wichMode) {
            Uri mUri = getContentResolver().insert(ProductEntry.CONTENT_URI, values);
            if (mUri != null) {
                userAdvice(getString(R.string.product_add));
            } else {
                userAdvice(getString(R.string.product_saved_failed));
            }

        } else {
            long rowsAffected = getContentResolver().update(mCurrentUri, values, null, null);
            if (rowsAffected > 0) {
                userAdvice(getString(R.string.edit_success));
            } else {
                userAdvice(getString(R.string.edit_failed));
            }
        }
        finish();
    }


    private void userAdvice(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_PROVIDER,
                ProductEntry.COLUMN_PRODUCT_PROVIDER_EMAIL,
                ProductEntry.COLUMN_PRODUCT_IMAGE
        };

        return new CursorLoader(this,
                mCurrentUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            // Acha as colunas de atributos produto em que estamos interessados
            int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int providerColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PROVIDER);
            int emailColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PROVIDER_EMAIL);
            int imageColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_IMAGE);

            // Extrai o valor do Cursor para o índice de coluna dado
            String product = cursor.getString(nameColumnIndex);

            double price = cursor.getDouble(priceColumnIndex);
            DecimalFormat numberFormat = new DecimalFormat("#.00");
            String sPrice = numberFormat.format(price);

            int quantity = cursor.getInt(quantityColumnIndex);
            String provider = cursor.getString(providerColumnIndex);
            String email = cursor.getString(emailColumnIndex);

            // Atualize as views na tela com os valores do banco de dados
            mNameEditText.setText(product);
            mPriceEditText.setText(sPrice);
            mQuantityTextView.setText(Integer.toString(quantity));
            mProviderEditText.setText(provider);
            mEmailEditText.setText(email);

            //Decodificação da imagem
            //Coloca o caminho da imagem em uma variavel global e em seguida chama o método para
            //colocar a imagem na tela.
            mCurrentPhotoPath = cursor.getString(imageColumnIndex);
            mCurrentPhotoPathUri = Uri.parse(mCurrentPhotoPath);
            mImageView.setImageBitmap(getBitmapFromUri(mCurrentPhotoPathUri));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mNameEditText.setText("");
        mPriceEditText.setText("");
        mQuantityEditText.setText("");
        mProviderEditText.setText("");
        mEmailEditText.setText("");
        mCurrentPhotoPath = "";
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Cria um AlertDialog.Builder e seta a mensagem, e click listeners
        // para os botões positivos e negativos do dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicou no botão "Continuar editando", então feche o dialog
                // e continue editando o product.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Cria e mostra o AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        // Se o produto não mudou, continue lidando com clique do botão back
        if (!mProductHasChanged) {
            super.onBackPressed();
            return;
        }

        // Caso contrário se há alterações não salvas, configure um dialog para alertar o usuário.
        // Crie um click listener para lidar com o usuário confirmando que mudanças devem ser descartadas.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicou no botão "Discard", fecha a activity atual.
                        finish();
                    }
                };

        // Mostra dialog dizendo que há mudanças não salvas
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new item, hide the "Delete" menu item.
        if (wichMode == true) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the product.
                deleteProduct();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the product.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the product in the database.
     */
    private void deleteProduct() {
        // Only perform the delete if this is an existing product.
        if (wichMode == false) {

            int rowsDeleted = getContentResolver().delete(mCurrentUri, null, null);
            // Mostra uma mensagem toast dependendo se ou não o delete foi bem sucedido.
            if (rowsDeleted == 0) {
                // Se nenhum registro foi deletado, então houve um erro com o delete.
                Toast.makeText(this, getString(R.string.delete_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Caso contrário, o delete foi bem sucedido e podemos mostrar um toast.
                Toast.makeText(this, getString(R.string.delete_success),
                        Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

    /**
     * Checks the user android version and creates an intent after that
     */
    public void openImageSelector() {
        Intent intent;

        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }

        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {

            if (resultData != null) {
                 mCurrentPhotoPathUri = resultData.getData();
                 mCurrentPhotoPath = mCurrentPhotoPathUri.toString();
                 mImageView.setImageBitmap(getBitmapFromUri(mCurrentPhotoPathUri));
            }
        }
    }

    /**
     * Checks if the resource exists, then resize the file and return the new bitmap
     * @param uri
     * @return
     */
    public Bitmap getBitmapFromUri(Uri uri) {

        if (uri == null || uri.toString().isEmpty()) {
            Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.image_placeholder);
            return b;
        }

        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        InputStream input = null;
        try {
            input = this.getContentResolver().openInputStream(uri);
            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, null, bmOptions);
            input.close();

            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            input = this.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, bmOptions);
            input.close();

            return bitmap;

        } catch (FileNotFoundException fne) {
            mCurrentPhotoPath = "";
            Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.image_placeholder);
            return b;
        } catch (Exception e) {
            mCurrentPhotoPath = "";
            Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.image_placeholder);
            return b;
        }
    }
}