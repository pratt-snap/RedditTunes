import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Router } from '@angular/router';
import { SongsService } from '../services/songs.service';

@Component({
  selector: 'app-redditor',
  templateUrl: './redditor.component.html',
  styleUrls: ['./redditor.component.scss'],
})
export class RedditorComponent implements OnInit {
  loading: boolean = false; 
  redditorId: string = ''; 

  constructor(private http: HttpClient,private router: Router, private songService:SongsService) {}

  ngOnInit(): void {
  }

  submitRedditorName() {
    this.loading = true;
    const queryParams = new HttpParams().set('userId', this.redditorId);

    this.http.post('http://localhost:8080/redditor',  null, { params: queryParams })
        .subscribe(
          (response: any) => {
            console.log('Post request successful', response);
            this.songService.setData(response);
            this.loading = false;
            this.router.navigate(['/suggestions']);
            },
            (error: any) => {
                console.error('Error while making post request', error);
            }
        );
}

}
