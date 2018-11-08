package example.com.ballidaku.commonClasses;

public interface CommonInterfaces
{
    public default void onResponse(String response)
    {}

    public default void onSuccess()
    {}

    public default void onFailure()
    {}

    public default void onChange(String oldPassword,String newPassword)
    {}


}
