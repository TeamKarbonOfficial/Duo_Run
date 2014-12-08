Duo Run
=======

**NOTE: TO CHECK FOR COLLISION, USE**
```java
Intersect.overlapConvexPolygons();//!!!!
```
**Images for Balls & Icons: (Place images in \android\assets)**
http://teamkarbon.com/cloud/public.php?service=files&t=69d7aca788e27f04971fad1bd79a314c
**File of Shared Stuff** includes graphics, pics, etc.. :P
http://teamkarbon.com/cloud/public.php?service=files&t=739066b6a6d5000a7b929fb72e55a859

**Music (still in progress)**
https://soundcloud.com/euwbah/duo-run-game-music-preview

That's all folks...

[![Build Status](https://travis-ci.org/TeamKarbonOfficial/GDXTest.svg?branch=master)](https://travis-ci.org/TeamKarbonOfficial/GDXTest)

TODO
=======
- Replace Ball with sprites
- Make the obstacles.  **DONE**
- Make them work. **DONE**
    - Get polygons done **done**
    - Get them to render via filled triangles **done**
    - Get them to have the proper colours and collision checks **done**
    - Make them bigger **done**
    - Make them spawn properly **done**
    - Make them less laggy...  **done**
    - Make a proper scoring system based on area of obstacles covered **done**
- Find out a way to check for collisions **done**
- Actually check for collisions **done**
- Make an endgame situation function
    - Extend the CustomGUIBox to fit 4 buttons in a 2 x 2 button matrix. **DONE**
    - Display a proper integer score in a rather convincing font. **In progress**
    - Display a 2 x 2 button matrix consisting of "Leaderboard", "Menu", "Play Again" and "Achievements" **In progress*
    - Figure out if that Achievements button should be in the main menu instead... :P
- Create levels **done**
- Create a level select screen **DONE**
    - Make proper GUI **DONE**
- Create a main menu screen
    - Create an interface. **done**
        - The user collides with the buttons using the respectively coloured balls to "click" the buttons. **done**
        - Make the interface work **done**
    - GO! button **DONE**
    - Options button
    - About button
    - Exit button *?*
    - Gamer Services button
    - Help button
        - Note: This button is a... button... Use helpbutton.png for this one.
- Have some options/whatever
- Add Game Service **In progress**
    - Get necessary libraries **Done**
    - Log in to Game Services **Done**
    - Add at least 5 achievements (Required by Playstore) **suggestions?**
    - Add Events??
    - Add leaderboards for all the gamemodes **Done**
- Test it on other devices too (to make sure the resolutions work and that the graphics aren't off-screen)
