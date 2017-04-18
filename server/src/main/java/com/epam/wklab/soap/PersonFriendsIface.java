package com.epam.wklab.soap;

import com.epam.wklab.person.Friends;
import com.epam.wklab.person.Person;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * Created by sten on 11.04.17.
 */
@WebService
public interface PersonFriendsIface {
    /**
     * @param p Person to get friends of year #year
     * @param year Checked year
     * @return Friends generated bean class instance
     * @throws NoMatchedFriendsException if there is no friends
     * matching given year
     */
    @WebMethod
    @WebResult(name = "friends")
    Friends getFriends(
            @WebParam(name = "person") Person p,
            @WebParam(name = "year") Integer year)
                throws NoMatchedFriendsException;
}
