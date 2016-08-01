package models;

/**
 *
 */
public abstract class Mutable {

    protected boolean muteNotifications;

    /**
     * This is the constructor for the class Mutable.
     *
     * @param muteNotification
     */
    public Mutable(boolean muteNotification) {
        this.muteNotifications = muteNotification;
    }
    
    /**
     * This method returns the Notification preference.
     *
     * @return
     */
    public boolean isMuted() {
        return muteNotifications;
    }
    
    public void mute() {
        muteNotifications = true;
    }
    
    public void unmute() {
        muteNotifications = false;
    }
    
    public void setMuted(boolean muted) {
        muteNotifications = muted;
    }

}
