# Campus Management Application

## Description of project
This project aims to provide a room reservation program for the buildings in TU Delft, but it is also configurable to be used in any other facility.
The project consists of two parts: the server running on Spring boot, in which a connection is made to a database running MYSQL;
and the client, which provides interactivity and easy connection to the server for the user, with JavaFX.
The user functionality is also implemented, with the users being able to log in, reserve rooms, food and bikes;
and the admin user can use all the CRUD functions.

The project supports implementation for rooms, buildings, reservations, users, facilities and bookings. In addition, these values also can be updated, deleted, or created; as well as be filtered by the user depending on the parameters that they input.

## Group members

| 📸 | Name | Email |
|---|---|---|
| ![](https://secure.gravatar.com/avatar/684c9e21fe9ad90b8db4b5e367d4522b?s=800&d=identicon&name=OOPP&length=4&size=50&color=DDD&background=777&font-size=0.325) | Sven van den Berg | S.T.vandenBerg@student.tudelft.nl |
| ![](https://secure.gravatar.com/avatar/e4b013974481d3287b5f152a6c2318bc?s=800&d=identicon&name=OOPP&length=4&size=50&color=DDD&background=777&font-size=0.325) | Patrick Hibbs | p.j.hibbs@student.tudelft.nl |
| ![](https://secure.gravatar.com/avatar/0875b15007013d48ce951184d0bd17e9?s=800&d=identicon&name=OOPP&length=4&size=50&color=DDD&background=777&font-size=0.325) | Halvard Mørstad | h.s.s.morstad@student.tudelft.nl |
| ![](https://secure.gravatar.com/avatar/b579391cd4f740bf04d01c34c0c0d369?s=800&d=identicon&name=OOPP&length=4&size=50&color=DDD&background=777&font-size=0.325) | Shirley Li | S.X.Li@student.tudelft.nl |
| ![](https://secure.gravatar.com/avatar/74b45fdb633e5e3e92a8e40c40c5069d?s=800&d=identicon&name=OOPP&length=4&size=50&color=DDD&background=777&font-size=0.325) | Alexandru Toader | A.Toader@student.tudelft.nl |
| ![](https://secure.gravatar.com/avatar/25bb90af11a76524a605818d10d44fb4?s=800&d=identicon&name=OOPP&length=4&size=50&color=DDD&background=777&font-size=0.325) | Chuckyin Sin | C.Y.Sin@student.tudelft.nl |
| ![](https://secure.gravatar.com/avatar/efe0359bd4b8804701c770e56ce7b97b?s=800&d=identicon&name=OOPP&length=4&size=50&color=DDD&background=777&font-size=0.325) | Alp Cicimen | G.A.Cicimen@student.tudelft.nl |

## How to run it
To run the program, the user first needs to set up a MYSQL database.
* Makefile (recommended): `make db_start`
* Docker: ```docker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=pwd -e MYSQL_DATABASE=oopp -v `pwd`/data:/var/lib/mysql --rm --name oopp-db mysql:latest```
* Local MySQL (not recommended): [mac](https://duckduckgo.com/?q=mysql+mac) | [linux](https://duckduckgo.com/?q=mysql+linux) | [windows](https://duckduckgo.com/?q=mysql+windows)  . 

If the Makefile/Docker is changed or not used, make sure to place the correct information in the `application.properties` inside of server

`/server/src/main/resources/application.properties`

Start the server, and after `Seeding complete` shows, start the client

## How to contribute to it
* This project uses checkstyle for formatting, you can find it in `/config/checkstyle/checkstyle.xml`

## Copyright / License (opt.)
