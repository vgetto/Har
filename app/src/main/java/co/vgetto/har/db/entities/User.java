package co.vgetto.har.db.entities;

import auto.parcel.AutoParcel;

/**
 * Created by Kovje on 11.9.2015..
 */
@AutoParcel
public abstract class User {
    public abstract String email();

    public static User create(String email) {
        return new AutoParcel_User(email);
    }
}
