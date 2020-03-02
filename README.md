# Starting template

This README will need to contain a description of your project, how to run it, how to set up the development environment, and who worked on it.
This information can be added throughout the course, except for the names of the group members.
Add your own name (do not add the names for others!) to the section below.

## Description of project

## Group members

| 📸 | Name | Email |
|---|---|---|
| ![](https://eu.ui-avatars.com/api/?name=OOPP&length=4&size=50&color=DDD&background=777&font-size=0.325) | Sven | S.T.vandenBerg@student.tudelft.nl |
| ![](https://eu.ui-avatars.com/api/?name=OOPP&length=4&size=50&color=DDD&background=777&font-size=0.325) | Patrick Hibbs | p.j.hibbs@student.tudelft.nl |
| ![](https://eu.ui-avatars.com/api/?name=OOPP&length=4&size=50&color=DDD&background=777&font-size=0.325) | Halvard Mørstad | h.s.s.morstad@student.tudelft.nl |
| ![](https://eu.ui-avatars.com/api/?name=OOPP&length=4&size=50&color=DDD&background=777&font-size=0.325) | Shirley Li | S.X.Li@student.tudelft.nl |
| ![](https://eu.ui-avatars.com/api/?name=OOPP&length=4&size=50&color=DDD&background=777&font-size=0.325) | Alexandru Toader | A.Toader@student.tudelft.nl |
| ![](https://eu.ui-avatars.com/api/?name=OOPP&length=4&size=50&color=DDD&background=777&font-size=0.325) | Chuckyin Sin | C.Y.Sin@student.tudelft.nl |
| ![](https://secure.gravatar.com/avatar/efe0359bd4b8804701c770e56ce7b97b?s=800&d=identicon&name=OOPP&length=4&size=50&color=DDD&background=777&font-size=0.325) | Alp Cicimen | G.A.Cicimen@student.tudelft.nl |

<!-- Instructions (remove once assignment has been completed -->
<!-- - Add (only!) your own name to the table above (use Markdown formatting) -->
<!-- - Mention your *student* email address -->
<!-- - Preferably add a recognisable photo, otherwise add your GitLab photo -->
<!-- - (please make sure the photos have the same size) --> 

## How to run it

## How to contribute to it

## Copyright / License (opt.)

## Docker configuration

docker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=pwd -e MYSQL_DATABASE=oopp -v `pwd`/data:/var/lib/mysql --rm --name oopp-db mysql:latest

## Get the make command 

1. Download make-4.1-2-without-guile-w32-bin.zip from https://sourceforge.net/projects/ezwinports/
2. Extract zip.
3. Copy the contents to your Git\mingw64\ merging the folders, but do NOT overwrite/replace any existing files.