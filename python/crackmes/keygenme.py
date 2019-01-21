def create_password(username):
    al = 0x5
    retstr = ""
    for ecx in range(0, len(username)):
        print "username[%d] = %s; al=%d" % (ecx, username[ecx], al)
        bl = ord(username[ecx])
        bh = bl
        bl |= al
        al = bh
        retstr += chr(bl)
    return retstr

