package com.headmostlab.notes.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentId;

import java.util.Date;

public class Note implements Parcelable {
    @DocumentId
    private String id;
    private String title;
    private String description;
    private Date creationDate;

    public Note() {
    }

    public Note(String id, String title, String description, Date creationDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.creationDate = creationDate;
    }

    protected Note(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        creationDate = new Date(in.readLong());
    }

    @Override
    public String toString() {
        return "Note{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }

    public String toHumanString() {
        return String.format("%s\n%s\n%s\n%s", id, title, description, creationDate);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeLong(creationDate.getTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public static class Builder {

        private final Note note;

        public Builder() {
            note = new Note();
        }

        public Builder setId(String id) {
            note.id = id;
            return this;
        }

        public Builder setTitle(String title) {
            note.title = title;
            return this;
        }

        public Builder setDescription(String description) {
            note.description = description;
            return this;
        }

        public Builder setCreateDate(Date creationDate) {
            note.creationDate = creationDate;
            return this;
        }

        public Note build() {
            return note;
        }
    }
}
