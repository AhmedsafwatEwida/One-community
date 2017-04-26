package pioneers.safwat.onecommunity;

/**
 * Created by safwa on 2/16/2017.
 */

public class Userinformations {
    int _id ;
    String _LAT ;
    String _LNG ;
    String _NAME ;
    String _AGE ;
    String _GENDRE ;
    String _HEFZ ;
    String _STUDIES;
    String _VOLUNTEER;
    String _SPORTS;
    String _GUID ;
    String _NATIONALITY ;

    public Userinformations(int id, String LAT, String LNG,String name, String age, String gendre, String hefz,
                            String studies , String volun , String sports, String guid , String nation) {
        this._id=id;
        this._LAT=LAT;
        this._LNG=LNG;
        this._NAME=name;
        this._AGE=age;
        this._GENDRE=gendre;
        this._HEFZ=hefz;
        this._STUDIES=studies;
        this._VOLUNTEER=volun;
        this._SPORTS=sports;
        this._GUID=guid;
        this._NATIONALITY=nation;
    }
    public Userinformations(String LAT, String LNG, String name, String age, String gendre,
                            String hefz, String studies , String volun , String sports,String guid , String nation) {
        this._LAT=LAT;
        this._LNG=LNG;
        this._NAME=name;
        this._AGE=age;
        this._GENDRE=gendre;
        this._HEFZ=hefz;
        this._STUDIES=studies;
        this._VOLUNTEER=volun;
        this._SPORTS=sports;
        this._GUID=guid;
        this._NATIONALITY=nation;
    }
    public int getuserid(){
        return this._id;
    }
    public String getuserlat(){
        return this._LAT;
    }
    public String getuserlng(){
        return this._LNG;
    }
    public String getuserName(){
        return this._NAME;
    }
    public String getuserage(){
        return this._AGE;
    }
    public String getusergendre(){ return this._GENDRE; }
    public String gethefzi(){
        return this._HEFZ;
    }
    public String getstudies(){ return this._STUDIES; }
    public String getvolun(){
        return this._VOLUNTEER;
    }
    public String getsports(){ return this._SPORTS; }
    public String getguid(){
        return this._GUID;
    }
    public String getnation(){ return this._NATIONALITY; }

    public void setuserid(int userid){this._id=userid;}
    public void setuserlat(String userlat){
        this._LAT=userlat;
    }
    public void setuserlng(String userlng){
        this._LNG=userlng;
    }
    public void setuserName(String username){this._NAME=username;}
    public void setuserage(String userage){
        this._AGE=userage;
    }
    public void setusergendre(String usergendre){this._GENDRE=usergendre; }
    public void sethefzi(String hefz){this._HEFZ=hefz;}
    public void setstudies(String studies){this._STUDIES=studies; }
    public void setvolun(String volun){
        this._VOLUNTEER=volun;
    }
    public void setsports(String sports){this._SPORTS=sports; }
    public void setguid(String guid){
        this._GUID=guid;
    }
    public void setnation(String nation){this._NATIONALITY=nation; }

}