package com.tutorials.dao;

import com.tutorials.entity.Tutorial;
import com.tutorials.exceptions.DataBaseOperationException;
import com.tutorials.exceptions.TutorialNotFoundException;

import java.util.ArrayList;

public interface TutorialDAO {
    void addTutorial(Tutorial tutorial) throws DataBaseOperationException;
    Tutorial getTutorialById(int id) throws TutorialNotFoundException, DataBaseOperationException;
    ArrayList<Tutorial> getAllTutorials() throws DataBaseOperationException;
    void updateTutorial(Tutorial tutorial) throws TutorialNotFoundException, DataBaseOperationException;
    void deleteTutorial(int id) throws TutorialNotFoundException, DataBaseOperationException;
}
