package br.com.arthurgrangeiro.saopaulo;

/**
 * Created by Arthur on 07/01/2017.
 */

public class TouristAttraction {
    /**
     * Resource ID for the image
     */
    private int placeImage;

    /**
     * The name of the tourist attraction
     */
    private String placeName;

    /**
     * The address and phone number to the tourist attraction
     */
    private String contactInfo;

    /**
     * Blank class constructor
     */
    public TouristAttraction() {
    }

    /**
     * Class constructor with all parameters
     *
     * @param placeImage
     * @param placeName
     * @param contactInfo
     */
    public TouristAttraction(int placeImage, String placeName, String contactInfo) {
        this.placeImage = placeImage;
        this.placeName = placeName;
        this.contactInfo = contactInfo;
    }

    /**
     * Return the resource id to the object image
     *
     * @return
     */
    public int getPlaceImage() {
        return placeImage;
    }

    /**
     * Set a new resource id to the object image
     *
     * @param placeImage
     */
    public void setPlaceImage(int placeImage) {
        this.placeImage = placeImage;
    }

    /**
     * Return a string with the object name
     *
     * @return
     */
    public String getPlaceName() {
        return placeName;
    }

    /**
     * Set a new string name to the object
     *
     * @param placeName
     */
    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    /**
     * Return the object contact info
     *
     * @return
     */
    public String getContactInfo() {
        return contactInfo;
    }

    /**
     * Set a new contat info to the object
     *
     * @param contactInfo
     */
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    @Override
    public String toString() {
        return "TouristAttraction{" +
                "Image Id=" + placeImage +
                ", Place Name='" + placeName + '\'' +
                ", Contact Info='" + contactInfo + '\'' +
                '}';
    }
}
