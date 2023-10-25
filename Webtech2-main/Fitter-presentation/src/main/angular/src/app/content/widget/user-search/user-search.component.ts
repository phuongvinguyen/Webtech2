import { Component, Input, OnInit } from '@angular/core';
import { UserSearchService, UserProfile } from './user-search.service';
import { UserCardService } from '../user-card/user-card.service';
import { EmojiComponent } from '@ctrl/ngx-emoji-mart/ngx-emoji';
import { AppComponent } from 'src/app/app.component';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { FormsModule, ReactiveFormsModule} from '@angular/forms';


@Component({
    selector: 'app-user-search',

    template:`
    <form class="form-inline my-2 my-lg-0 list-header" (ngSubmit)="search2()" #searchForm="ngForm">
      <input class="form-inline my-2 my-lg-0 list-header" value=""   type="text" [(ngModel)]="query" name="query" placeholder="Suche nach Benutzern">
      <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Suchen</button>
    </form>

    <ul *ngIf="userProfiles">
      <li *ngFor="let userProfile of userProfiles"> </li>
    </ul>`

  })

  export class UserSearchComponent {
  query = ""; 
  userProfiles: UserProfile[];

  constructor(private userSearchService: UserSearchService,
    private userCardService: UserCardService) { }

  search() {
    this.userSearchService.searchUsers(this.query)
      .subscribe((userProfiles: UserProfile[]) => {
        this.userProfiles = userProfiles;
      });
  }

  search2() {
    this.userCardService.refreshUserList(this.query)
  }

/*   search2() {
    if (this.query = null){
      this.userCardService.refreshUserList('')
    } else {
      this.userCardService.refreshUserList(this.query)

    }

  } */

    
        
    }
  
    /* search() {
      this.userSearchService.searchUsers(this.query)
        .subscribe((userProfiles: UserProfile[]) => {
          this.userProfiles = userProfiles;
        });
    } */
  