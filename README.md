Duo Run
=======

**NOTE: TO CHECK FOR COLLISION, USE**
```java
Intersect.overlapConvexPolygons();//!!!!
```

**FLOOBIT!!!! WOAW!!**
www.floobits.com
Download the IntelliJ Plugin in Android Studios File -> Setting -> IDE Settings -> Plugins -> Search "Floobit" -> Browse -> Download
Make a new Floobit account on the website using ur existing GitHub account...
Floobit URL: https://floobits.com/TeamKarbon/Duo_Run
          
[![Floobits Status](https://floobits.com/TeamKarbon/Duo_Run.png)](https://floobits.com/TeamKarbon/Duo_Run/redirect)

**Ad SDK**
http://teamkarbon.com/cloud/public.php?service=files&t=9075431d4857542adedfc9410c994a29

**Images for Balls, Icons, Sliders & background shapes: (Place images in \android\assets)**
http://teamkarbon.com/cloud/public.php?service=files&t=69d7aca788e27f04971fad1bd79a314c

**File of Shared Stuff** includes graphics, pics, etc.. :P
http://teamkarbon.com/cloud/public.php?service=files&t=739066b6a6d5000a7b929fb72e55a859

**default.png file**
https://github.com/libgdx/libgdx/blob/master/tests/gdx-tests-android/assets/data/default.png

**Music (still in progress) (Look around for those tracks entitled Duo Run)**
https://soundcloud.com/euwbah/

**Get SHA-1 from KeyStore**
https://gist.github.com/darkpolice/0e1f24ec490c7329cea5

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
- Find out a way to check for collisions **DONE**
- Actually check for collisions **done**
- Make an endgame situation function
    - Extend the CustomGUIBox to fit 4 buttons in a 2 x 2 button matrix. **DONE**
    - Display a proper integer score in a rather convincing font. **DONE**
        - Make some cool animation when the score changes **Done, gotta make it slower tho**
    - Display a 2 x 2 button matrix consisting of "Leaderboard", "Main Menu", "Play Again" and "Achievements" **DONE**
        - Fix lotsa bugs with Leaderboard and Achievements button **DONE**
        - Make sure the Main Menu button and Achievements button actually work. **DONE**
- Create levels **TODO**
    - Gradually increase difficulty as game time passes.
        - Increase speed
        - Increase Spawn rate
        - Spawn obstacles that move (Might be a little complex tho XD)
- Create a level select screen **DONE**
    - Make proper GUI **DONE**
- Create a main menu screen
    - Create an interface. **DONE**
        - The user collides with the buttons using the respectively coloured balls to "click" the buttons. **DONE**
        - Make the interface work **DONE**
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
    - Add at least 5 achievements (Required by Playstore) **Done**
    - Add Events??
    - Add leaderboards for all the gamemodes **Done**
    - In-game money *In the quite far future*
        - Make them as collectibles in the game + more with higher score
        - Earned with achievements unlocked
        - Buy upgrades (Applies in multiplayer mode as well!)
            - Revive
            - Increased control (level 1 - 9; level 10 = control of both up and down movements for both balls)
            - Slow-mo (spawns collectibles in-game which temporarily slows the obstacles {not the whole game} down; level <=> frequency of spawn)
            - One-colour (spawns collectibles in-game which renders all the obstacles to a single colour for 5s)
    - Multiplayer mode *In the far future*
        - One player controls one ball, but during the game, the two players will switch which sprite they're controlling
        - Custom Sprites. (Put random picture, and tint the picture so that the user will know which faction the user is in)
    - Redeem code **(10^9001.337)! / (3 - (1011b XOR 1000)) years later**
- Test it on other devices too (to make sure the resolutions work and that the graphics aren't off-screen)
