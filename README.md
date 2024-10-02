# Forex Agile

## How To Run

1. Create a `.env` files in `/src/main/resources` directory and supply the
   required variables for configurations in `application.properties`. You can
   use `.env.example` as a template.

2. Choose a Maven profile and install the dependencies. Three profiles are
   available:

   - `prod`: use MySQL driver (default profile)
   - `dev-mariadb`: use MariaDB driver
   - `dev-postgres`: use PostgreSQL driver

   You can skip choosing a profile, and the default profile will be used.

   If you use Eclipse, import the project and choose a Maven profile. Check
   [here](#choosing-maven-profile-in-eclipse) to see how you can choose a Maven
   profile in Eclipse.

   On the command line, you can run `mvn clean install -P <profile>` to install
   dependencies for a particular profile. The `-P <profile>` specifies the Maven
   profile you use.

3. Create your own RSA public and private keys and save them a
   `/src/main/resources/certs` as `public.pem` and `private.pem` respectively.
   The repo comes with two sample keys for the convenience of local testing, but
   you should replace them with your own generated keys for production.

4. Run the project:

   - Eclipse: Right click `ForexApplication.java` -> `Run As` ->
     `Java Application`
   - Command line: `mvn -P <maven-profile> spring-boot:run`

### Choosing Maven Profile in Eclipse

To import a new Maven project and choose the Maven profile:

1. `File` from menu -> `Import...` -> `Existing Maven Projects`
2. `Browse...` and select the project directory
3. Open `Advanced` section
4. Fill in the desired profile in `Profiles` section
5. Click `Finish`

To change the Maven profile for an existing project:

1. Right click on the project in the Project Explorer and click `Properties`
2. In `Maven` tab, fill in the desired profile in `Active Maven Profiles`
3. `Apply`

## Building a docker image locally and run it

First, follow step 1 **and** 3 from [How To Run](#how-to-run) section, to set up
the `.env` and generate a pair of RSA keys.

Then to use H2 memory as database:

```bash
mvn clean package -P "ci-h2"
docker build -t <docker-image-name> .
docker run --env-file src/main/resources/.env --env RSA_PUBLIC_KEY="$(cat src/main/resources/certs/public.pem)" --env RSA_PRIVATE_KEY="$(cat src/main/resources/certs/private.pem)" -p <host-port>:<container-port> --rm <docker-image-name>
```

Here, `<container-port>` is the port the application inside the container runs
on, which should be the same as `SERVER_PORT` in the `.env` file. `<host-port>`
defines the port on the host machine that the container port should be mapped
to.
