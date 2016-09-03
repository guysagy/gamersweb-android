package com.guysagy.gamersweb.user;

final public class User 
{
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String password;
    private String dob;
    private String gender;
    private String avatarFileName;
    
    private User()
    {
    }
    
    public static User createUser()
    {
        return new User();
    }
    
    public User setFirstName(String firstName)
    {
        this.firstName = firstName;
        return this;
    }
    
    public String getFirstName()
    {
        return this.firstName;
    }
    
    public User setLastName(String lastName)
    {
        this.lastName = lastName;
        return this;
    }
    
    public String getLastName()
    {
        return this.lastName;
    }
    
    public User setUserName(String userName)
    {
        this.userName = userName;
        return this;
    }
    
    public String getUserName()
    {
        return this.userName;
    }	
    
    public User setEmail(String email)
    {
        this.email = email;
        return this;
    }
    
    public String getEmail()
    {
        return this.email;
    }	
    
    public User setPassword(String password)
    {
        this.password = password;
        return this;
    }
    
    public String getPassword()
    {
        return this.password;
    }	
    
    public User setDob(String dob)
    {
        this.dob = dob;
        return this;
    }
    
    public String getDob()
    {
        return this.dob;
    }	
    
    public User setGender(String gender)
    {
        this.gender = gender;
        return this;
    }
    
    public String getGender()
    {
        return this.gender;
    }		
    
    public User setAvatarFileName(String avatarFileName)
    {
        this.avatarFileName = avatarFileName;
        return this;
    }
    
    public String getAvatarFileName()
    {
        return this.avatarFileName;
    }
    
    @Override
    public int hashCode()
    {
        return 7 + (new StringBuilder(getUserName()))
                                        .append(getEmail())
                                        .append(getPassword())
                                        .append(getDob())
                                        .append(getGender())
                                        .append(getAvatarFileName())
                                        .hashCode();
    }

    public boolean equalStrings(String string1, String string2)
    {
        if ((string1 == null) ? (string2 != null) : !string1.equals(string2))
        {
            return false;
        }
        return true;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        
        if (getClass() != obj.getClass())
        {
            return false;
        }

        final User other = (User) obj;
        
        if (!equalStrings(this.getUserName(), other.getUserName()))
            return false;

        if (!equalStrings(this.getEmail(), other.getEmail()))
            return false;
        
        if (!equalStrings(this.getPassword(), other.getPassword()))
            return false;
        
        if (!equalStrings(this.getDob(), other.getDob()))
            return false;
        
        if (!equalStrings(this.getGender(), other.getGender()))
            return false;
        
        if (!equalStrings(this.getAvatarFileName(), other.getAvatarFileName()))
            return false;
        
        if (!equalStrings(this.getUserName(), other.getUserName()))
            return false;        

        return true;
    }
}
