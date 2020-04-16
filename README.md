## What is this project for?  
I created these applications for the *FIRST* Robotics competition during my time on Team 2471 Mean Machine. These applications are for scouting, which is the process of collecting data on the robots of other teams. We do this because this gives us an edge during the Qualifying matches because we know what the other robots on the opposing team can do. Three-fourths of the way into competition, we enter a phase called *Alliance Selection*. While the teams of three robots on each side are randomized for every match during the Qualifying matches, teams are finalized during Alliance Selection. This is the most important part of scouting; we can use the data to objectively determine the best robots for our Alliance, instead of relying on seed rankings which can be formed in part due to chance.  
## Strange style of Code?
I wrote this while I was learning Java, and I had not yet learned the proper conventions and design strategies that I have learned since in my Computer Science classes. The style may be strange, but this project was a Java and project building learning experience.
## How do these applications work together?
A diagram of the life cycle of our scouting data and the use of the applications in this life cycle is laid out in *scoutingsystems.jpeg*.
- I developed **NirvanaScouter** as the Android UI (we had Voyager tablets available), to collect the data. This data is stored locally (Wi-Fi and hot spots were disabled since they interfered with the robots) as .txt files. 
- **DataProcessorAndAnalysis** processes the data into a workbook, a collection of spreadsheets, and calculates a few statistics. 
- **DataVisualization** takes the data from the workbook and provides various visualization methods, including time series for a robot on an individual variable, the ability to draft an team of three robots and see the combined performance, and ranking on individual variables/combined variables.
## Why Java over Python
I know that Python would be better suited for this project, however, I chose Java for a few reasons. The first reason is for consistency. Our team operates on Java for all of our programming, including the code on the robot, and Android uses Java. My second reason is because our AP Computer Science class teaches Java, so if students of that class wanted to transition to robotics, they would have a much smoother time helping me develop the scouting system. 
