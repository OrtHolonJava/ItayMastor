# Itay Mastor's Java Project
Name: Final Days

It should play like this game: [Link](http://store.steampowered.com/app/459830/Final_Days/)

## Opening/Importing the game project
Use [IntelliJ IDEA](https://www.jetbrains.com/idea/).

1. Open IntelliJ (or close all open projects).
2. Press **Import Project**.
3. Choose the **build.gradle** file (thats inside the FinalDays folder).

([Source of the instructions above](https://github.com/libgdx/libgdx/wiki/Gradle-and-Intellij-IDEA))  
[Instructions for Eclipse](https://github.com/libgdx/libgdx/wiki/Gradle-and-Eclipse) (Not recommended)

## Running The Game
1. `Run -> Edit Configurations...`
2. Click the plus (+) button and select `Application`.
3. * Set the `Name` to `Desktop`.
    * Set the field `Use classpath of module` to `desktop` (or `desktop_main`)
    * Click on the button of the `Main class` field and select the `DesktopLauncher` class.
    * Set the `Working directory` to the `your_project_path/core/assets/` folder.
    * Click `Apply` and then `OK`.
4. You have now created a run configuration for your desktop project. You can now select the configuration and run it.
