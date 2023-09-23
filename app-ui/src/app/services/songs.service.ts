import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SongsService {
  private songs: any = [];

  setData(data: any) {
    this.songs = data;
  }

  getData() {
    return this.songs;
  }
}
