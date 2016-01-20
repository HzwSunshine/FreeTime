package com.hzwsunshine.freetime.Zero;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 测试Parcelable
 * Created by 何志伟 on 2016/1/6.
 */
public class User implements Parcelable {
    private int age;
    private String userName;
    private boolean isMale;

//    private Book mBook;

    public User(int age, String userName, boolean isMale) {
        this.age = age;
        this.userName = userName;
        this.isMale = isMale;
    }

    protected User(Parcel in) {
        age=in.readInt();
        userName=in.readString();
        isMale=in.readInt()==1;
//        mBook=in.readParcelable(Thread.currentThread().getContextClassLoader()- Loader());
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        /**
         * 从序列化的对象中创建原始对象
         */
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        /**
         * 创建指定长度的原始对象数组
         */
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    /**
     * 返回当前对象的描述
     */
    @Override
    public int describeContents() {
        return 0;
    }


    /**
     * 将当前对象写入序列化结构中
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(age);
        dest.writeString(userName);
        dest.writeInt(isMale ? 1:0);
//        dest.writeParcelable(mBook,0);
    }
}
