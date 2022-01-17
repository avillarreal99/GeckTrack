package com.example.gecktrack.ui.dashboard;

public class GeckoModel
{

// PRIVATE INSTANCE VARIABLES ----------------------------------------------------------------------


    private  int    ID;
    private  String name;
    private  String sex;
    private  String birthday;
    private  String age;
    private  String geckoSpecies;
    private  String morph;
    private  String weight;
    private  String temperature;
    private  String humidity;
    private  String status;
    private  String seller;
    private  String photoID;


// CONSTRUCTOR -------------------------------------------------------------------------------------

    // creating a new gecko constructor
    public GeckoModel(int ID, String name, String sex, String birthday, String age,
                      String geckoSpecies, String morph, String weight, String temperature,
                      String humidity, String status, String seller, String photoID)
    {
        this.ID            = ID;
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

    // getting list of geckos constructor (My Gecks Page list)
    public GeckoModel(String name, String sex, String age, String geckoSpecies, String morph, String photoID)
    {
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.geckoSpecies = geckoSpecies;
        this.morph = morph;
        this.photoID = photoID;
    }

// GETTERS AND SETTERS -----------------------------------------------------------------------------


    public int getID()
    {
        return ID;
    }

    public void setID(int ID)
    {
        this.ID = ID;
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

    public String getAge()
    {
        return age;
    }

    public void setAge(String age)
    {
        this.age = age;
    }

    public String getWeight()
    {
        return weight;
    }

    public void setWeight(String weight)
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

    public String getTemperature()
    {
        return temperature;
    }

    public void setTemperature(String temperature)
    {
        this.temperature = temperature;
    }

    public String getHumidity()
    {
        return humidity;
    }

    public void setHumidity(String humidity)
    {
        this.humidity = humidity;
    }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getSeller()
    {
        return seller;
    }

    public void setSeller(String purchasedFrom)
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
                "  ID = '" + ID + '\'' + "\n" +
                "  name = '" + name + '\'' + "\n" +
                "  sex = " + sex + "\n" +
                "  birthday = '" + birthday + '\'' + "\n" +
                "  age = " + age + "\n" +
                "  species = " + geckoSpecies + "\n" +
                "  morph = '" + morph + '\'' + "\n" +
                "  weight = '" + weight + '\'' + "\n" +
                "  temperature = " + temperature + "\n" +
                "  humidity = " + humidity + "\n" +
                "  status = " + status + "\n" +
                "  seller = '" + seller + '\'' + "\n" +
                "  photoID = '" + photoID + '\'' + "\n" +
                "}\n\n";
    }
}
