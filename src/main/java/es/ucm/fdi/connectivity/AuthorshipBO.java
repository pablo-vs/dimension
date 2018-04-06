package es.ucm.fdi.connectivity;

/**
 * Represents a relationship of authorship between an user and a shared project.
 *
 * @author Pablo Villalobos
 * @version 04.04.2018
 */
public class AuthorshipBO {
        /**
         * Author of the project
         */
        private String author;
        /**
         * Shared project
         */
        private String project;
        /**
         * Authorship id
         */
        private String id;

        /**
         * Class constructor specifying author and shared project.
         * Id is made by the joint of user and project
         * @param user Author
         * @param proj Shared project
         */
        public AuthorshipBO(String user, String proj) {
                author = user;
                project = proj;
                id = user + project;
        }

        /**
         * 
         * @return the id of the authorship
         */
        public String getId() {
                return id;
        }

        /**
         * 
         * @return the author of the project
         */
        public String getAuthor() {
                return author;
        }

        /**
         * 
         * @return the shared project
         */
        public String getProject() {
                return project;
        }
}
