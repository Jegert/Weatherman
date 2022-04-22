import {AfterViewInit, Component} from '@angular/core';
import * as L from "leaflet";
import axios from "axios";

export interface Result {
  provider: string;
  country: string;
  city: string;
  temperature: number;
  rainFall: string;
  weatherType: string;
}

export interface ApiProvider {
  name: string;
  checked: boolean;
}

@Component({
  selector: 'app-weather-form',
  templateUrl: './weather-form.component.html',
  styleUrls: ['./weather-form.component.css']
})
export class WeatherFormComponent implements AfterViewInit {
  private map: any;
  private marker: any;
  public latitude: number = 0;
  public longitude: number = 0;
  public results: Result[] = [];
  public apiProviders: ApiProvider[] = [
    {name: "AccuWeather", checked: true}, // AccuWeather will be used as a "true weather" source
    {name: "OpenWeatherMap", checked: false},
    {name: "Tomorrow.io", checked: false},
    {name: "Weatherbit.io", checked: false}
  ];

  private initMap(): void {
    this.map = L.map('map', {
      center: [58.54, 25.35],
      zoom: 3
    });

    const tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 18,
      minZoom: 3,
      attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    });

    tiles.addTo(this.map);

    //Handles clicking on map
    this.map.on('click', (e: any) => {
      const coord = e.latlng;
      this.latitude = coord.lat;
      this.longitude = coord.lng;
      this.showOnMap();
    });
  }

  showOnMap() {
    this.map.flyTo([this.latitude, this.longitude], 8);

    //removes last marker
    if (this.marker != undefined) {
      this.map.removeLayer(this.marker);
    }

    this.marker = L.marker([this.latitude, this.longitude]).addTo(this.map);
  }

  getResults() {
    let providers = [];
    this.results.length = 0;
    for (const provider of this.apiProviders) {
      if (provider['checked']) {
        providers.push(provider['name'].replace(".", ""));
      }
    }

    axios.get('http://localhost:8080/weatherforecast', {
      params: {
        providers: providers.join(","),
        latitude: this.latitude,
        longitude: this.longitude
      }
    }).then(res => {
      for (const api of res.data) {
        this.results.push({provider: api.Provider, city: api.City, country: api.Country, rainFall: api.RainFall, temperature: api.Temperature, weatherType: api.WeatherType});
      }
    })
  }

  updateLat(event: any) {
    this.latitude = event.target.value;
  }

  updateLon(event: any) {
    this.longitude = event.target.value;
  }

  toggleCheckBox(checkbox: any) {
    if (checkbox['name'] != "AccuWeather") {
      checkbox['checked'] = !checkbox['checked'];
    }
  }

  ngAfterViewInit(): void {
    this.initMap();
  }

}
