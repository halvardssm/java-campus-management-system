# Starting template

This README will need to contain a description of your project, how to run it, how to set up the development environment, and who worked on it.
This information can be added throughout the course, except for the names of the group members.
Add your own name (do not add the names for others!) to the section below.

## Description of project
This project aims to provide a room reservation program for the buildings in TU Delft, but it is also configurable to be used in any other facility.
The project consists of two parts, the server, in which a connection is made to a database running MYSQL, and the client, which provides interactivity and easy connection to the server for the user, with JavaFX.
The user functionality is also implemented, with the users being able to log in, reserve rooms, food and bikes; and the admin user can use all the CRUD functions.

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

<!-- Instructions (remove once assignment has been completed -->
<!-- - Add (only!) your own name to the table above (use Markdown formatting) -->
<!-- - Mention your *student* email address -->
<!-- - Preferably add a recognisable photo, otherwise add your GitLab photo -->
<!-- - (please make sure the photos have the same size) --> 

## How to run it
To run the program, the user first needs to set up a MYSQL database.
Then, the user should create a user that is at least allowed the basic CRUD operations,
and the name and password of the user should be inputted in the properties file in 

server/src/main/resources/application.properties.

Then, the server should be started. After that, the client can be used to access the server.

## How to contribute to it

## Copyright / License (opt.)
