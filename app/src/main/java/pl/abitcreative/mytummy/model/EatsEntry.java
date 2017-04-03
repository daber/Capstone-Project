package pl.abitcreative.mytummy.model;

import android.support.annotation.Keep;

/**
 * Created by mdabrowski on 03/04/17.
 */

@Keep
public class EatsEntry {
  String placeId;
  String placeName;
  long   timeStamp;

  public String getPlaceId() {
    return placeId;
  }

  public void setPlaceId(String placeId) {
    this.placeId = placeId;
  }

  public String getPlaceName() {
    return placeName;
  }

  public void setPlaceName(String placeName) {
    this.placeName = placeName;
  }

  public long getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(long timeStamp) {
    this.timeStamp = timeStamp;
  }
}
