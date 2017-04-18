package com.epam.wklab.soap;

import com.epam.wklab.person.Friends;
import com.epam.wklab.person.Person;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by sten on 11.04.17.
 */
@WebService
public class PersonFriends implements PersonFriendsIface {

    /**
     *
     * @param p Person to get friends of year #year
     * @param year Checked year
     * @return Friends generated bean
     * @throws NoMatchedFriendsException
     */
    @WebMethod
    @Override
    public final Friends getFriends(final Person p, final Integer year)
            throws NoMatchedFriendsException {

        Friends matchedFriends = new Friends();
        for (Person f : p.getFriends().getPerson()) {
            if (f.getBirth().getYear() == year) {
                matchedFriends.getPerson().add(f);
            }
        }
        if (matchedFriends.getPerson().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("No matched friends of person \"");
            sb.append(p.getName());
            sb.append("\" which were born in year ");
            sb.append(Integer.toString(year));
            throw new NoMatchedFriendsException(sb.toString());
        }
        return matchedFriends;
    }
}
