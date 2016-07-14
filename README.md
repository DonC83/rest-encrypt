# rest-encrypt

###Encyption/Decryption with key pair

This was an actual requirement for some integration work that was required for a third-party to post data in json
format to a destination to be processed. The payload was to be encrypted by the source using a public key provided by
the destination, which would then use the private key that it created to decrypt the payload and then process it.

Information about security in Java can be found here [Java Security Tutorial](https://docs.oracle.com/javase/tutorial/security/index.html)
I don't find the tutorial the be the best source, the use case demonstrated seems very narrow in my opinion.


I used OpenSSL to generate my key pairs. If you're using a Linux based OS you can read up on generation options by doing
```
man openssl
```
For convenience the commands I used to generate my key pairs are below:

Create the private key without a passphrase:
```
openssl genrsa -out pvt.key 2048
```

Create the public key from the private key:
```
openssl rsa -pubout -in pvt.key -out pub.key
```

Application is used to demonstrate consuming json that has been encrypted with a public/private key pair.


