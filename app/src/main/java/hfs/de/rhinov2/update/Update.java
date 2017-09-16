package hfs.de.rhinov2.update;

import android.text.TextUtils;

import lombok.Getter;

/**
 * Created by study on 16.09.17.
 */
@Getter
public class Update {
    private final String title;
    private final String description;

    public Update(final String title, final String description){
        if (TextUtils.isEmpty(title)) {
            throw new IllegalArgumentException("title null or empty!");
        }
        if (TextUtils.isEmpty(description)) {
            throw new IllegalArgumentException("description null or empty!");
        }

        this.title = title;
        this.description = description;
    }
}
