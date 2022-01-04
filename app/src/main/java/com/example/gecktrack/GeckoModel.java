package com.example.gecktrack;

public class GeckoModel
{

// PRIVATE INSTANCE VARIABLES ----------------------------------------------------------------------


    private  int    geckoID;
    private  String name;
    private  String geckoSpecies;  // acquired through Spinner
    private  String sex;
    private  String birthday;
    private  int    age;
    private  double weight;
    private  String morph;
    private  int    temperature;
    private  int    humidity;
    private  String status;
    private  String seller;
    private  String photoID;


// CONSTRUCTOR -------------------------------------------------------------------------------------


    public GeckoModel(int geckoID, String name, String geckoSpecies, String sex, String birthday,
                      int age, double weight, String morph, int temperature, int humidity,
                      String status, String seller, String photoID)
    {
        this.geckoID       = geckoID;
        this.name          = name;
        this.geckoSpecies  = geckoSpecies;
        this.sex           = sex;
        this.birthday      = birthday;
        this.age           = age;
        this.weight        = weight;
        this.morph         = morph;
        this.temperature   = temperature;
        this.humidity      = humidity;
        this.status        = status;
        this.seller        = seller;
        this.photoID       = photoID;
    }


// GETTERS AND SETTERS -----------------------------------------------------------------------------


    public int getGeckoID()
    {
        return geckoID;
    }

    public void setGeckoID(int geckoId)
    {
        this.geckoID = geckoId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getGeckoSpecies()
    {
        return geckoSpecies;
    }

    public void setGeckoSpecies(String geckoSpecies)
    {
        this.geckoSpecies = geckoSpecies;
    }

    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getBirthday()
    {
        return birthday;
    }

    public void setBirthday(String birthday)
    {
        this.birthday = birthday;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public double getWeight()
    {
        return weight;
    }

    public void setWeight(double weight)
    {
        this.weight = weight;
    }

    public String getMorph()
    {
        return morph;
    }

    public void setMorph(String morph)
    {
        this.morph = morph;
    }

    public int getTemperature()
    {
        return temperature;
    }

    public void setTemperature(int temperature)
    {
        this.temperature = temperature;
    }

    public int getHumidity()
    {
        return humidity;
    }

    public void setHumidity(int humidity)
    {
        this.humidity = humidity;
    }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getPurchasedFrom()
    {
        return seller;
    }

    public void setPurchasedFrom(String purchasedFrom)
    {
        this.seller = purchasedFrom;
    }

    public String getPhotoID()
    {
        return photoID;
    }

    public void setPhotoID(String photoID)
    {
        this.photoID = photoID;
    }


// TOSTRING ----------------------------------------------------------------------------------------


    @Override
    public String toString()
    {
        return "GeckoModel\n" +
                "{\n" +
                "  geckoID = " + geckoID + "\n" +
                "  name = '" + name + '\'' + "\n" +
                "  species = " + geckoSpecies + "\n" +
                "  sex = " + sex + "\n" +
                "  birthday = '" + birthday + '\'' + "\n" +
                "  age = " + age + "\n" +
                "  morph = '" + morph + '\'' + "\n" +
                "  temperature = " + temperature + "\n" +
                "  humidity = " + humidity + "\n" +
                "  status = " + status + "\n" +
                "  seller = '" + seller + '\'' + "\n" +
                "  photoID = '" + photoID + '\'' + "\n" +
                "}\n\n";
    }
}
