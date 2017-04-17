package com.epam.wklab.client;

import com.epam.wklab.person.Friends;
import com.epam.wklab.person.Person;
import com.epam.wklab.person.ObjectFactory;
import com.epam.wklab.soap.GetFriends;
import com.epam.wklab.soap.NoMatchedFriendsException_Exception;
import com.epam.wklab.soap.PersonFriendsIface;
import com.epam.wklab.soap.PersonFriendsService;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

/**
 * Created by sten on 12.04.17.
 */
public class TestServer {

    private static Scanner s = new Scanner(System.in);
    private static ObjectFactory obf = new ObjectFactory();
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);
    private static final String ENDPOINT_ADDRESS = "http://localhost:7070/soapservice/PersonFriends";

    private static String getValueOf(String what) {
        System.out.print(what + ":");
        return s.nextLine();
    }

    private static Person createPerson() {
        Person p = obf.createPerson();
        p.setName(getValueOf("Person's name"));
        boolean gotBirth = false;
        while(!gotBirth) {
            String birth = getValueOf("Person's birth (" + DATE_FORMAT + ")");
            try {
                Date birthDate = DATE_FORMATTER.parse(birth);
                gotBirth = true;
                GregorianCalendar c = new GregorianCalendar();
                c.setTime(birthDate);
                XMLGregorianCalendar xc = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
                p.setBirth(xc);
            } catch (ParseException e) {
                System.err.println("Wrong date format");
            } catch (DatatypeConfigurationException e) {
                throw new RuntimeException(e);
            }
        }

        p.setFriends(new Friends());
        boolean chooseEndFriends = false;
        while(!chooseEndFriends) {
            System.out.println("[a] - add new friend, [e] - end of forming person " + p.getName());
            String choose = s.nextLine();
            if (choose.toLowerCase().contains("a")) {
                System.out.println("*** Creating new friend ***");
                p.getFriends().getPerson().add(createPerson());
                System.out.println("*** Friend is created ***");
            } else if (choose.toLowerCase().contains("e")) {
                chooseEndFriends = true;
            }
        }
        return p;
    }

    public static void main(String[] args) {

        Person p = createPerson();

        System.out.print("Year to select friends: ");
        Integer year = Integer.parseInt(s.nextLine());

        System.out.print("Hit enter to send");
        s.nextLine();

        GetFriends.Person sendPerson = new GetFriends.Person();
        sendPerson.setBirth(p.getBirth());
        sendPerson.setFriends(p.getFriends());
        sendPerson.setName(p.getName());

        PersonFriendsService service = new PersonFriendsService();
        PersonFriendsIface port = service.getPersonFriendsPort();
        BindingProvider bindingProvider = (BindingProvider) port;
        bindingProvider.getRequestContext().put(
                BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                ENDPOINT_ADDRESS);
        try {
            Friends response = port.getFriends(sendPerson, year);
            final int friendsCount = response.getPerson().size();
            System.out.println("Matched friends count " + friendsCount);

            for(Person f : response.getPerson()) {
                printFriend(f);
            }
        } catch (NoMatchedFriendsException_Exception e) {
            System.err.println("No friends matched year " + year);
            System.err.println("Server says:" + e);
        }

    }

    private static void printFriend(Person p) {
        System.out.println("Friend name: " + p.getName());
        System.out.println("Friend birth: " +
                DATE_FORMATTER.format(p.getBirth().toGregorianCalendar().getTime()));
    }
}
