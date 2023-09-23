import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SongsService } from '../services/songs.service';

@Component({
  selector: 'app-suggestion-list',
  templateUrl: './suggestion-list.component.html',
  styleUrls: ['./suggestion-list.component.scss']
})
export class SuggestionListComponent implements OnInit {

  songs: any[] = [];
  
  constructor(private route: ActivatedRoute,private songService:SongsService) { }

  ngOnInit(): void {
    this.songs=this.songService.getData();
    console.log(this.songs);
  }
}
