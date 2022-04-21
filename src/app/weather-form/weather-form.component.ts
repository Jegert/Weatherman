import {AfterViewInit, Component} from '@angular/core';
import * as L from "leaflet";

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
  public date: any = new Date().toISOString().slice(0, 19); //Gets current date, slices off the ISO string Z
  public apiProviders: any = [
    {name: "OpenWeatherMap", checked: false},
    {name: "Tomorrow.io", checked: false},
    {name: "Stormglass", checked: false},
    {name: "Yahoo Weather", checked: false},
    {name: "AccuWeather", checked: false}
  ];

  private initMap(): void {
    this.map = L.map('map', {
      center: [ 39.8282, -98.5795 ],
      zoom: 3
    });

    const tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 18,
      minZoom: 3,
      attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    });

    tiles.addTo(this.map);

    //Handles clicking on map
    this.map.on('click', (e: any) =>{
      const coord = e.latlng;
      this.latitude = coord.lat;
      this.longitude = coord.lng;
      this.showOnMap();
      console.log(this.apiProviders);
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

  updateLat(event: any) {
    this.latitude = event.target.value
  }

  updateLon(event: any) {
    this.longitude = event.target.value
  }

  toggleCheckBox(checkbox: any) {
    checkbox['checked'] = !checkbox['checked'];
  }

  constructor() {
  }

  ngAfterViewInit(): void {
    this.initMap();
  }

}
