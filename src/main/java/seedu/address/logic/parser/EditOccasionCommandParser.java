package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OCCASIONDATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OCCASIONLOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OCCASIONNAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditOccasionCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditOccasionCommand object
 */
public class EditOccasionCommandParser implements Parser<EditOccasionCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditOccasionCommand
     * and returns an EditOccasionCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditOccasionCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_OCCASIONNAME, PREFIX_OCCASIONDATE,
                        PREFIX_OCCASIONLOCATION, PREFIX_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EditOccasionCommand.MESSAGE_USAGE), pe);
        }

        EditOccasionCommand.EditOccasionDescriptor editOccasionDescriptor =
                 new EditOccasionCommand.EditOccasionDescriptor();
        if (argMultimap.getValue(PREFIX_OCCASIONNAME).isPresent()) {
            editOccasionDescriptor.setOccasionName(ParserUtil.parseOccasionName(
                    argMultimap.getValue(PREFIX_OCCASIONNAME).get()));
        }
        if (argMultimap.getValue(PREFIX_OCCASIONDATE).isPresent()) {
            editOccasionDescriptor.setOccasionDate(ParserUtil.parseOccasionDate(
                    argMultimap.getValue(PREFIX_OCCASIONDATE).get()));
        }
        if (argMultimap.getValue(PREFIX_OCCASIONLOCATION).isPresent()) {
            editOccasionDescriptor.setOccasionLocation(ParserUtil.parseOccasionLocation(
                    argMultimap.getValue(PREFIX_OCCASIONLOCATION).get()));
        }
        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editOccasionDescriptor::setTags);

        if (!editOccasionDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditOccasionCommand.MESSAGE_NOT_EDITED);
        }

        return new EditOccasionCommand(index, editOccasionDescriptor);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }
}