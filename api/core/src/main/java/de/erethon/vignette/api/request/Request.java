/*
 * Written from 2019-2020 by Daniel Saukel
 *
 * To the extent possible under law, the author(s) have dedicated all
 * copyright and related and neighboring rights to this software
 * to the public domain worldwide.
 *
 * This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication
 * along with this software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package de.erethon.vignette.api.request;

import de.erethon.vignette.api.GUI;
import java.util.Date;

/**
 * @deprecated draft
 * @author Daniel Saukel
 */
@Deprecated
public class Request {

    private RequestParticipator requester;
    private RequestParticipator target;
    private Date expiration;
    private GUI requestGUI;

    /**
     * @param requester  the RequestParticipator that poses the Request
     * @param target     the RequestParticipator that the Request is addressed to
     * @param expiration when the Request expires and is denied automatically
     * @param requestGUI the GUI to open to the target to pose the Request
     */
    public Request(RequestParticipator requester, RequestParticipator target, Date expiration, GUI requestGUI) {
        this.requester = requester;
        this.target = target;
        this.expiration = expiration;
        this.requestGUI = requestGUI;
    }

    /**
     * Creates a Request that does not expire.
     *
     * @param requester  the RequestParticipator that poses the Request
     * @param target     the RequestParticipator that the Request is addressed to
     * @param requestGUI the GUI to open to the target to pose the Request
     */
    public Request(RequestParticipator requester, RequestParticipator target, GUI requestGUI) {
        this.requester = requester;
        this.target = target;
        this.expiration = new Date(-1l);
        this.requestGUI = requestGUI;
    }

    /**
     * Returns the RequestParticipator that poses the Request.
     *
     * @return the RequestParticipator that poses the Request
     */
    public RequestParticipator getRequester() {
        return requester;
    }

    /**
     * Returns the RequestParticipator that the Request is addressed to.
     *
     * @return the RequestParticipator that the Request is addressed to
     */
    public RequestParticipator getTarget() {
        return target;
    }

    /**
     * Returns true if the Request is expired; false if not.
     *
     * @return true if the Request is expired; false if not
     */
    public boolean isExpired() {
        return expiration.getTime() != -1 && System.currentTimeMillis() > expiration.getTime();
    }

    /**
     * Returns the GUI to open to the target to pose the Request.
     *
     * @return the GUI to open to the target to pose the Request
     */
    public GUI getRequestGUI() {
        return requestGUI;
    }

    /**
     * Sends the Request to the target.
     */
    public void send() {
        requestGUI.open(target);
    }

}
