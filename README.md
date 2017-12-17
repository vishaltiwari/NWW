# About
In this work, we present a 4D GIS system to visualize space-time dependent phenomena - simulated hydrological water flow model over an urban area. This work attempts to use the calculated water depth information to present a nearreal visual rendering of the same, with the emphasis on the visual interaction of the 3D objects (buildings) with such phenomenon captured in 4D (space and time). The developed system is built using the NASA’s WorldWind Globe and uses a depth filling algorithm as its input for time-step generated water depth maps, the dynamic layer. The urban scene is derived from a static CityGML LOD2 buildings layer overlaid on the digital elevation map. The dynamic flow visualization is enabled through an appropriate color mapping scheme so that the user can have a fair sense of water depth at various areas of the city over the time period. While visualization helps to understand the phenomenon and its progress, it is also important to provide an appropriate mechanism to derive or extract the relevant technical information from such a system. Towards this, in the developed system, analytical tools like querying for water depth at any given time, displaying of hydrographs showing the variation of height over time at a given location and a slider to control the time parameter of the system, have been incorporated. As the system uses 3DcityDB as the storage model, it is highly expendable for answering complex queries like ”which buildings of a specific area of the city will get flooded and when ?”. Such queries are not yet built in and is left for further work. 

Link to older webpage: http://lsi.iiit.ac.in/worldwind/?page_id=8

For more details: http://web2py.iiit.ac.in/research_centres/publications/view_publication/mastersthesis/481

# Project Setup
- Open this project in your eclipse as : File -> import -> From Existing Project into Workspace, and point to the clone directory.

- Start the application with setting the GDAL environment variables and the required libraries and start src/StartUpGUI.java main.
export GDAL_DATA="", or one can load them in the main function.

- Add the rendercitygml project. link: https://github.com/vishaltiwari/rendercitygml, as it uses this to render buildings.

- To generate the Digital Surface Model, you can make use of the export Elevation option. But you first need to have your building data loaded into 3DCityDB. (You need to setup the postgres DB and POSTGIS extension, followed by setting up 3DCityDB)

- After generating the DSM, src/generateData can create a series of depth maps which can be used for renderering. Or you can use any hydrological simulation model on top of the DSM. Generate the depth rasters images at different time intervals and feed it to the system.

- Using the Animate button, one can specify the path of the time series generated data. Specify the digital surface model, sample rate and overall time in mins/hours of the simulation in real world.

- One can query for depth values and create hydrographs by clicking on points on interest on the ROI.

Note: This is a framework for the visualization of your hydrological simulation in an Urban setting. We have used a depth filling algorithm for quick testing of our visualization framework.

# Video Links

[![Top View](https://i.imgur.com/zJNXNIj.png)](https://youtu.be/U7mCD743LYI)


[![Slant View](https://i.imgur.com/Hh9HWPk.png)](https://youtu.be/TcXzJTHYB1g)


[![Street View](https://i.imgur.com/n0XCuAe.png)](https://youtu.be/XkrBYgpxB8k)


