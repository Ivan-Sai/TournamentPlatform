import {Component, OnInit} from '@angular/core';
import {AllTournamentRequestData} from "../../../../models/request/AllTournamentRequestData";
import {TournamentService} from "../../../../services/TournamentService";
import {AsyncPipe, NgForOf, NgIf, NgStyle} from "@angular/common";
import {Observable} from "rxjs";
import {TournamentRequestData} from "../../../../models/request/TournamentRequestData";
import {data} from "autoprefixer";
import {ActivatedRoute, Router} from "@angular/router";
import {FormsModule} from "@angular/forms";
import {TournamentAllListComponent} from "../tournament-all-list/tournament-all-list.component";

@Component({
  selector: 'app-tournaments-all',
  templateUrl: './tournaments-all.component.html',
  standalone: true,
  imports: [
    NgForOf,
    AsyncPipe,
    NgIf,
    NgStyle,
    FormsModule,
    TournamentAllListComponent
  ],
  styleUrls: ['./tournaments-all.component.css']
})
export class TournamentsAllComponent implements OnInit{
  selectedFilter: string = 'upcoming';
  selectedDiscipline: string = 'all';
  public isRefresh = false;


  constructor(private router:Router,
              ){
  }

  ngOnInit(): void {
  }



  handleSelection() {
    this.isRefresh = false;
    this.router.navigateByUrl('tournaments?filter=' + this.selectedFilter + '&discipline=' + this.selectedDiscipline);
  }

  refreshFilters(url: string) {
    this.router.navigateByUrl(url);
    this.isRefresh = true;
  }
}
