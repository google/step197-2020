package com.google.codeu.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import java.io.IOException;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Returns charging station data a JSON array, e.g. [{"lat": 38.4404675, "lng":
 * -122.7144313}]
 */
@WebServlet("/charging-station-data")
public class ChargingStationServlet extends HttpServlet {

  private JsonArray chargingStationArray;

  @Override
  public void init() {
    chargingStationArray = new JsonArray();
    Gson gson = new Gson();
    Scanner scanner = new Scanner(getServletContext().getResourceAsStream(
        "/WEB-INF/charging_stations.csv"));
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String[] cells = line.split(",");

      double lat = Double.parseDouble(cells[0]);
      double lng = Double.parseDouble(cells[1]);

      chargingStationArray.add(gson.toJsonTree(new ChargingStation(lat, lng)));
    }
    scanner.close();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    response.setContentType("application/json");
    response.getOutputStream().println(chargingStationArray.toString());
  }

  // This class could be its own file if we needed it outside this servlet
  private static class ChargingStation {
    double lat;
    double lng;

    private ChargingStation(double lat, double lng) {
      this.lat = lat;
      this.lng = lng;
    }
  }
}
