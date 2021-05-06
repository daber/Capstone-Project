package pl.abitcreative.mytummy.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

/**
 * Created by mdabrowski on 03/04/17.
 */

@Keep
public class EatsEntry implements Parcelable {
  String placeId;
  String placeName;
  String id;
  long   timeStamp;

  public EatsEntry() {

  }

  protected EatsEntry(Parcel in) {
    placeId = in.readString();
    placeName = in.readString();
    id = in.readString();
    timeStamp = in.readLong();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(placeId);
    dest.writeString(placeName);
    dest.writeString(id);
    dest.writeLong(timeStamp);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<EatsEntry> CREATOR = new Creator<EatsEntry>() {
    @Override
    public EatsEntry createFromParcel(Parcel in) {
      return new EatsEntry(in);
    }

    @Override
    public EatsEntry[] newArray(int size) {
      return new EatsEntry[size];
    }
  };

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

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
