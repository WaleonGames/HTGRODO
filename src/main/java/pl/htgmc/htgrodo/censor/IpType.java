package pl.htgmc.htgrodo.censor;

public enum IpType {
    NONE,        // nie IP
    PRIVATE,     // domowe / lokalne
    PUBLIC,      // publiczne użytkownika
    VPN_OR_HOSTING // publiczne – możliwy VPN / DC
}
