package com.sp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class GateApp
{
    public static void main( String[] args )
    {

        SpringApplication.run( GateApp.class, args );

        System.out.println( "Hello World!" );
    }
}
