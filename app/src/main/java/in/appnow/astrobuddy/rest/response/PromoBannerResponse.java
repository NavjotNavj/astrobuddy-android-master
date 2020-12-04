package in.appnow.astrobuddy.rest.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sonu on 17:00, 10/04/19
 * Copyright (c) 2019 . All rights reserved.
 */
public class PromoBannerResponse extends BaseResponseModel {
    @SerializedName("response")
    private List<PromoBanner> promoBannerList;

    public List<PromoBanner> getPromoBannerList() {
        return promoBannerList;
    }

   public class PromoBanner implements Parcelable {
        @SerializedName("id")
        private int id;
        @SerializedName("title")
        private String title;
        @SerializedName("description")
        private String description;
        @SerializedName("img")
        private String image;
        @SerializedName("type")
        private String type;
        @SerializedName("action")
        private String action;
        @SerializedName("seq")
        private int seq;

       protected PromoBanner(Parcel in) {
           id = in.readInt();
           title = in.readString();
           description = in.readString();
           image = in.readString();
           type = in.readString();
           action = in.readString();
           seq = in.readInt();
       }

       public  final Creator<PromoBanner> CREATOR = new Creator<PromoBanner>() {
           @Override
           public PromoBanner createFromParcel(Parcel in) {
               return new PromoBanner(in);
           }

           @Override
           public PromoBanner[] newArray(int size) {
               return new PromoBanner[size];
           }
       };

       public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public int getSeq() {
            return seq;
        }

        public void setSeq(int seq) {
            this.seq = seq;
        }

       @Override
       public int describeContents() {
           return 0;
       }

       @Override
       public void writeToParcel(Parcel parcel, int i) {
           parcel.writeInt(id);
           parcel.writeString(title);
           parcel.writeString(description);
           parcel.writeString(image);
           parcel.writeString(type);
           parcel.writeString(action);
           parcel.writeInt(seq);
       }
   }
}
