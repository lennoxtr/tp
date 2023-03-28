package seedu.address.logic.commands.add;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_MODULE_DOES_NOT_EXIST;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.lecture.Lecture;
import seedu.address.model.lecture.LectureName;
import seedu.address.model.lecture.ReadOnlyLecture;
import seedu.address.model.module.Module;
import seedu.address.model.module.ModuleCode;
import seedu.address.model.module.ReadOnlyModule;
import seedu.address.testutil.ModelStub;
import seedu.address.testutil.TypicalLectures;
import seedu.address.testutil.TypicalModules;

public class AddLectureCommandTest {

    @Test
    public void constructor_nullModuleCode_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new AddLectureCommand(null, TypicalLectures.getCs2040sWeek1()));
    }

    @Test
    public void constructor_nullLecture_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new AddLectureCommand(TypicalModules.getCs2040s().getCode(), null));
    }

    @Test
    public void execute_lectureAcceptedByModel_addSuccessful() throws CommandException {
        Module module = TypicalModules.getCs2040s();
        ModuleCode moduleCode = module.getCode();
        Lecture lecture = TypicalLectures.getSt2334Topic1();

        ModelStubAcceptingLectureAdded modelStub = new ModelStubAcceptingLectureAdded();

        CommandResult result = new AddLectureCommand(moduleCode, lecture).execute(modelStub);

        assertEquals(String.format(AddLectureCommand.MESSAGE_SUCCESS, moduleCode, lecture), result.getFeedbackToUser());
        assertEquals(Arrays.asList(module), modelStub.modulesAddedTo);
        assertEquals(Arrays.asList(lecture), modelStub.lecturesAdded);
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        AddLectureCommand command =
                new AddLectureCommand(TypicalModules.getCs2040s().getCode(), TypicalLectures.getCs2040sWeek1());

        assertThrows(NullPointerException.class, () -> command.execute(null));
    }

    @Test
    public void execute_moduleDoesNotExist_throwsCommandException() {
        ModuleCode moduleCode = TypicalModules.getCs2040s().getCode();
        ModelStub modelStub = new ModelStubNoModule();
        AddLectureCommand command = new AddLectureCommand(moduleCode, TypicalLectures.getCs2040sWeek1());

        assertThrows(CommandException.class,
                String.format(MESSAGE_MODULE_DOES_NOT_EXIST, moduleCode), ()
                        -> command.execute(modelStub));
    }

    @Test
    public void execute_duplicateLecture_throwsCommandException() {
        Module module = TypicalModules.getCs2040s();
        ModuleCode moduleCode = module.getCode();
        Lecture lecture = TypicalLectures.getCs2040sWeek1();

        ModelStub modelStub = new ModelStubWithLecture(module, lecture);
        AddLectureCommand command = new AddLectureCommand(moduleCode, lecture);

        assertThrows(CommandException.class,
                String.format(AddLectureCommand.MESSAGE_DUPLICATE_LECTURE, moduleCode), () ->
                command.execute(modelStub));
    }

    @Test
    public void equals() {
        ModuleCode moduleCode = TypicalModules.getCs2040s().getCode();
        AddLectureCommand addCs2040sW1LectureCommand =
                new AddLectureCommand(moduleCode, TypicalLectures.getCs2040sWeek1());
        AddLectureCommand addCs2040sW2LectureCommand =
                new AddLectureCommand(moduleCode, TypicalLectures.getCs2040sWeek2());

        // same object -> returns true
        assertTrue(addCs2040sW1LectureCommand.equals(addCs2040sW1LectureCommand));

        // same values -> returns true
        AddLectureCommand addCs2040sW1LectureCommandCopy =
                new AddLectureCommand(moduleCode, TypicalLectures.getCs2040sWeek1());
        assertTrue(addCs2040sW1LectureCommand.equals(addCs2040sW1LectureCommandCopy));

        // different types -> returns false
        assertFalse(addCs2040sW1LectureCommand.equals(1));

        // null -> returns false
        assertFalse(addCs2040sW1LectureCommand.equals(null));

        // different lecture -> return false
        assertFalse(addCs2040sW1LectureCommand.equals(addCs2040sW2LectureCommand));
    }

    /**
     * A {@code Model} stub that always accepts the lecture being added.
     */
    private class ModelStubAcceptingLectureAdded extends ModelStub {
        private final ArrayList<ReadOnlyModule> modulesAddedTo = new ArrayList<>();
        private final ArrayList<Lecture> lecturesAdded = new ArrayList<>();

        @Override
        public ReadOnlyModule getModule(ModuleCode code) {
            return TypicalModules.getTypicalTracker().getModule(code);
        }

        @Override
        public boolean hasModule(ModuleCode moduleCode) {
            return true;
        }

        @Override
        public boolean hasLecture(ModuleCode moduleCode, LectureName lectureName) {
            return false;
        }

        @Override
        public void addLecture(ReadOnlyModule module, Lecture toAdd) {
            requireAllNonNull(module, toAdd);

            modulesAddedTo.add(module);
            lecturesAdded.add(toAdd);
        }
    }

    /**
     * A {@code Model} stub that contains no module.
     */
    private class ModelStubNoModule extends ModelStub {
        @Override
        public boolean hasModule(ModuleCode code) {
            return false;
        }
    }

    /**
     * A {@code Model} stub that contains a single lecture.
     */
    private class ModelStubWithLecture extends ModelStub {
        private final ReadOnlyModule module;
        private final ReadOnlyLecture lecture;

        public ModelStubWithLecture(ReadOnlyModule module, ReadOnlyLecture lecture) {
            this.module = module;
            this.lecture = lecture;
        }

        @Override
        public ReadOnlyModule getModule(ModuleCode code) {
            return TypicalModules.getTypicalTracker().getModule(code);
        }

        @Override
        public boolean hasModule(ModuleCode code) {
            return true;
        }

        @Override
        public boolean hasLecture(ModuleCode moduleCode, LectureName lectureName) {
            return module.getCode().equals(moduleCode) && lecture.getName().equals(lectureName);
        }
    }

}
