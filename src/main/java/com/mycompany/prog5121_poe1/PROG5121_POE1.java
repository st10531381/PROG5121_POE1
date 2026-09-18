package com.mycompany.prog5121_poe1;

import java.util.Scanner;

public class PROG5121_POE1 {package com.mycompany.prog5121_poe1;

import java.util.Scanner;

public class PROG5121_POE1 {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        Login login = new Login();

        System.out.println("=================================");
        System.out.println("       ACCOUNT REGISTRATION");
        System.out.println("=================================");

        System.out.print("Enter your first name: ");
        String firstName = input.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = input.nextLine();

        System.out.print("Enter a username: ");
        String username = input.nextLine();

        System.out.print("Enter a password: ");
        String password = input.nextLine();

        System.out.print("Enter your South African cellphone number (+27): ");
        String cellPhoneNumber = input.nextLine();

        String registrationResult = login.registerUser(
                username,
                password,
                cellPhoneNumber,
                firstName,
                lastName
        );

        System.out.println();
        System.out.println(registrationResult);

        // Only continue to login if registration was successful
        if (registrationResult.equals("User successfully registered.")) {

            System.out.println();
            System.out.println("=================================");
            System.out.println("              LOGIN");
            System.out.println("=================================");

            System.out.print("Enter your username: ");
            String loginUsername = input.nextLine();

            System.out.print("Enter your password: ");
            String loginPassword = input.nextLine();

            System.out.println();
            System.out.println(
                    login.returnLoginStatus(loginUsername, loginPassword)
            );
        }

        input.close();
    }
}

}