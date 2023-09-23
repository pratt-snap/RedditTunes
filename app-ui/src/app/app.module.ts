import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormsModule } from '@angular/forms';
import { NavigationModule } from './navigation/navigation.module';
import { RedditorModule } from './redditor/redditor.module';
import { HttpClientModule } from '@angular/common/http';
import { SongsService } from './services/songs.service';
import { SuggestionListModule } from './suggestion-list/suggestion-list.module';


@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    CommonModule,
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    RedditorModule,
    NavigationModule,
    HttpClientModule,
    SuggestionListModule
  ],
  providers: [SongsService],
  bootstrap: [AppComponent]
})
export class AppModule { }
