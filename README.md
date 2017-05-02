# NWW
- Open this project in your eclipse as : File -> import -> From Existing Project into Workspace, and point to the clone directory.

- Start the application with setting the GDAL environment variables and the required libraries and start src/StartUpGUI.java main.
export GDAL_DATA="", or one can load them in the main function.

- Add the rendercitygml project. link: https://github.com/vishaltiwari/rendercitygml, as it uses this to render buildings.

- To generate the Digital Surface Model, you can make use of the export Elevation option. But you first need to have your building data loaded into 3dCityDB

- After generating the DSM, src/generateData can create a series of depth maps which can be used for renderering.

- Using the Animate button, one can specify the path of the time series generated data. The digital surface model. sample rate and overall time in mins/hours of the simulation in real world.

- One can query for depth values and create hydrographs by clicking on points on interest on the ROI.

Results:


[![Top View](http://img.youtube.com/vi/U7mCD743LYI/0.jpg)](https://youtu.be/U7mCD743LYI)

[![Slant View](http://img.youtube.com/vi/TcXzJTHYB1g/0.jpg)](https://youtu.be/TcXzJTHYB1g)

[![Street View](http://img.youtube.com/vi/XkrBYgpxB8k/0.jpg)](https://youtu.be/XkrBYgpxB8k)


