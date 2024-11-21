package com.quizapp.quizApp.model;

public enum QuestionCategory {
    GENERAL_KNOWLEDGE(9),
    BOOKS(10),
    FILM(11),
    MUSIC(12),
    MUSICALS_THEATRES(13),
    TELEVISION(14),
    VIDEO_GAMES(15),
    BOARD_GAMES(16),
    NATURE(17),
    COMPUTERS(18),
    MATHEMATICS(19),
    MYTHOLOGY(20),
    SPORTS(21),
    GEOGRAPHY(22),
    HISTORY(23),
    POLITICS(24),
    ART(25),
    CELEBRITIES(26),
    ANIMALS(27),
    VEHICLES(28),
    COMICS(29),
    GADGETS(30),
    ANIME_MANGA(31),
    CARTOONS_ANIMATIONS(32), // Fixed closing parentheses
    ANY(0);  // Default category for "Any"

    private final int categoryId;

    QuestionCategory(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    // Method to map category name (string) to the Category enum
    public static QuestionCategory fromString(String category) {
        for (QuestionCategory cat : QuestionCategory.values()) {
            if (cat.name().equalsIgnoreCase(category)) {
                return cat;
            }
        }
        return ANY;  // Return default "Any" if category name is invalid
    }
}
