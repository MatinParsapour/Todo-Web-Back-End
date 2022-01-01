package web.todo.ToDoWeb.service;

public interface FilledValidation {

    /**
     * Check if one field is empty or not
     * @param field any attribute needs to check
     * @return true if the field is empty
     */
    Boolean isEmpty(String field);

    /**
     * Check if one field is blank or not
     * @param field any attribute needs to check
     * @return true if the field is blank
     */
    Boolean isBlank(String field);

    /**
     * Check if one field is null or not
     * @param field any attribute needs to check
     * @return true if the field is null
     */
    Boolean isNull(String field);

    /**
     * Check if the erase white spaces the field is empty or not
     * @param field any attributes needs to check
     * @return true if the filed has only white spaces
     */
    Boolean isWhiteSpace(String field);

}
