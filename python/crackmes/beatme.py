def create_password(username):
    len_user = len(username)
    if len_user <= 3 or len_user >= 0xc:
        return "Invalid username length!"
    password = chr(len_user + 0x30)
    password += username[2]
    len_shifted = len_user >> 1
    for x in username:
        x_o = ord(x)
        x_o += len_shifted
        x_o += 1
        password += chr(x_o)
        
    len_pw = len(password)
    
    if len_pw < 0x5 or len_pw >= 0xc:
        return "Invalid password length!"
    
    return password

def decrypt_password(username, pw):
    password = list(pw)
    i = 0
    len_user = len(username)
    first_char = ord(password[i]) - 0x30
    if first_char == len_user:
        print "Lengths match, moving on"
    else:
        print "Length mismatch"
        return ""
    
    i += 1
    if password[i] == username[1]:
        print "second chars match, moving on"
    else:
        print "second char mismatch"
        return ""
    
    i += 1
    
    len_shifted = len(username) >> 1
    
    for c in range(i, len(password)):
        password[c] = chr(ord(password[c]) - 1)
        
    for c in range(i, len(password)):
        password[c] = chr(ord(password[c]) - len_shifted)
    
    i2 = 0
    
    for n in range(0, len(username)):
        if username[i2] != password[i]:
            print "username[%d] != password[%d] (%s <> %s)" % (i2, i, username[i2], password[i])
            return ""
        i += 1
        i2 += 1
        
    i -= 2
    i2 -= 2
    
    if username[i2] != password[i]:
        print "Last change to dance failed"
        return ""
    
    print "WIN!"




