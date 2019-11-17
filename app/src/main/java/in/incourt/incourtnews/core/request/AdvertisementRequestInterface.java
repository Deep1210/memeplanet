package in.incourt.incourtnews.core.request;

/**
 * Created by bhavan on 3/18/17.
 */

public interface AdvertisementRequestInterface {

    public void onAdvertisementSuccess(String advertisementModel);

    public void onAdvertisementError(Throwable t);

}
