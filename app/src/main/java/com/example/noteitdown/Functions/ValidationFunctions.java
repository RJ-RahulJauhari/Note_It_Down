package com.example.noteitdown.Functions;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

public class ValidationFunctions {

    public static boolean UserLoginValidationPass(EditText EmailField, EditText PasswordField, Context context){
        String Email = EmailField.getText().toString();
        String Password = PasswordField.getText().toString();
        char[] EmailChar = Email.toCharArray();
        boolean ValidationPassed = true;

        if((Email.isEmpty() || Password.isEmpty())){
            Toast.makeText(context,"Please fill the required fields to Login!",Toast.LENGTH_SHORT).show();
            ValidationPassed = false;
        }else if(Password.length() < 7){
            Toast.makeText(context,"Password Should be more than 7 Characters",Toast.LENGTH_SHORT).show();
            ValidationPassed = false;
        }else{
            for(int i = 0; i<EmailChar.length;i++){
                if(EmailChar[i] == '@'){
                    break;
                } else if (i == EmailChar.length-1) {
                    Toast.makeText(context,"Invalid Email ID",Toast.LENGTH_SHORT).show();
                    ValidationPassed = false;
                }
            }
        }
        return ValidationPassed;
    }

    public static boolean UserRegistrationValidation(EditText Name, EditText EmailField, EditText Password1, EditText Password2, Context context){
        boolean ValidationPassed = true;
        String Email = EmailField.getText().toString();
        char[] EmailChar = Email.toCharArray();
        if(Name.getText().toString().isEmpty()){
            Toast.makeText(context,"Name left blank",Toast.LENGTH_SHORT).show();
            ValidationPassed = false;
        }
        if(!Password1.getText().toString().equals(Password2.getText().toString())){
            Toast.makeText(context,"Passwords do not match",Toast.LENGTH_SHORT).show();
            ValidationPassed = false;
        } else if (Password1.getText().toString().length() < 7 || Password2.getText().toString().length() < 7) {
            Toast.makeText(context,"Passwords do not match",Toast.LENGTH_SHORT).show();
            ValidationPassed = false;
        }else{
            for(int i = 0; i<EmailChar.length;i++){
                if(EmailChar[i] == '@'){
                    break;
                } else if (i == EmailChar.length-1) {
                    Toast.makeText(context,"Invalid Email ID",Toast.LENGTH_SHORT).show();
                    ValidationPassed = false;
                }
            }
        }
        return ValidationPassed;

    }
}
