import {AfterViewInit, Component, Input} from '@angular/core';
import {Sort} from "@angular/material/sort";
import {LiveAnnouncer} from "@angular/cdk/a11y";
import {stringify} from "@angular/compiler/src/util";

export interface Result {
  provider: string;
  country: string;
  city: string;
  temperature: number;
  rainFall: string;
  weatherType: string;
  latitude: number;
  longitude: number;
}

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.css']
})
export class ResultsComponent implements AfterViewInit{

  @Input() results: Result[] = [];
  @Input() tableName: string = "";
  @Input() buttonClick!: () => void;

  sortedData: Result[];
  cur_lat: number = 0;
  cur_lon: number = 0;

  constructor(private _liveAnnouncer: LiveAnnouncer) {
    this.sortedData = this.results.slice();
  }

  sortData(sort: Sort) {
    const data = this.results.slice();
    if (!sort.active || sort.direction === '') {
      this.sortedData = data;
      return;
    }

    this.sortedData = data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'provider':
          return compare(a.provider, b.provider, isAsc);
        case 'weathertype':
          return compare(a.weatherType, b.weatherType, isAsc);
        case 'temperature':
          return compare(a.temperature, b.temperature, isAsc);
        case 'rainfall':
          return compare(a.rainFall, b.rainFall, isAsc);
        default:
          return 0;
      }
    });
  }

  ngAfterViewInit(): void {
    this.sortedData = this.results.slice();
    this.cur_lat = this.results[0].latitude;
    this.cur_lon = this.results[0].longitude;
  }
}

function compare(a: number | string, b: number | string, isAsc: boolean) {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}
