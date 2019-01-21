def power(base, upto, modulus=0):
    bin_power = binary(upto)
    b = 1
    for (i = len(bin_power); i >= 0; i--)
        if (modulus)
            b = (b * b) mod modulus
        else
            b = b * b

        if (bin_power[i] == 1):
            if (modulus)
                b = (b * a) mod modulus
            else
                b = b * a

    return b