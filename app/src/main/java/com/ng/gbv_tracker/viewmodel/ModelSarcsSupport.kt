package com.ng.gbv_tracker.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.ng.gbv_tracker.Event
import com.ng.gbv_tracker.model.SARCsSupport
import com.ng.gbv_tracker.room.DatabaseRoom
import com.ng.gbv_tracker.utils.ClassSharedPreferences
import kotlinx.coroutines.*


class ModelSarcsSupport(application: Application) : AndroidViewModel(application) {

    private val database = DatabaseRoom.getDatabaseInstance(application)
    private val viewModelJob = SupervisorJob()//OR Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val myDetails = ClassSharedPreferences(application).getCurUserDetail()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    init {
//        viewModelScope.launch {
//            addToDb(SarcSupports.sarcs)//Load data
//        }
    }

    val sarcsSupports = database.sARCsSupportDao.getAll()



    //Item category clicking
    private var _curItemCategory = MutableLiveData<Event<SARCsSupport>>()
    val curItemCategory:LiveData<Event<SARCsSupport>> get() = _curItemCategory
    fun setItemCategory(item:SARCsSupport){
        _curItemCategory.value = Event(item)
    }



    suspend fun addToDb(item: List<SARCsSupport> = SarcSupports.sarcs){
        withContext(Dispatchers.IO){
            try {
                database.sARCsSupportDao.upSert(item)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }









    /**
     * Factory for constructing DevByteViewModel with parameter
     */
    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ModelSarcsSupport::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ModelSarcsSupport(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}







object SarcSupports{
    val sarcs = mutableListOf<SARCsSupport>()

    init {
        sarcs.addAll(
            listOf(
                SARCsSupport(1,"Hope Center 1","Adamawa","ADSACA building State Dr Usha Saxena Specialist Hospital Jimeta, Yola",
                    "07068339913","hopecenteryola@gmail.com","",""
                    ,"Dr Usha Saxena","08069710461","ushasaxena@gmail.com,hopecenteryola@gmail.com"),

                SARCsSupport(2,"Hope Centre 2","Adamawa","General Hospital, Numan",
                    "09077777433, 09090000648, 09050797650, 09067773008","agapesarc@yahoo.com","","Twitter: @AgapeSarc"
                    ,"Paula G. Turnbwem  ","07035082504","paulagtumbwem@gmail.com"),

                SARCsSupport(3,"Agape Centre","Akwa Ibom","Immanuel General Hospital, Eket",
                    "09077777433, 09090000648, 09050797650, 09067773008","agapesarc@yahoo.com","","Twitter: @AgapeSarc"
                    ,"Anietie Ikpe  ","08023009220","anie_ntem@yahoo.com"),

                SARCsSupport(4,"Ntasi Centre","Anambra","General Hospital Enugwu-Ukwu Njikoka Local Govemment Area, Anambra",
                    "09066916915, 09049224086","","",""
                    ,"Bemadette Uchendu","08066306402","uchebern@yahoo.com"),

                SARCsSupport(5,"Nelewa Centre","Borno","Umar Shehu Ultra-modern Hospital, Maiduguri",
                    "08028982947","N31ewacentre@gmail.com","Twitter: @N31ewaCentre ","Facebook: @N31ewa center"
                    ,"Fati Mustapha","08151838555","mustaphafati209@gmail.com"),

                SARCsSupport(6,"Moremi Clinic","Ekiti","Moremi Clinic, ART building, Ekiti State University Teaching Hospital (EKSUTH), Ado-Ekiti",
                    "07050752287, 07039786904","ekitisarc@gmail.com","",""
                    ,"Barrister Rita Ilevbare","08033581144","ritailevbare@gmail.com"),

                SARCsSupport(7,"Tamar SARC","Enugu","Family Support Programme Building, Opposite College Of Education Technical, Abakaliki Road, Enugu",
                    "08060084441 , 07032567458","ensgtamarsarc@yahoo.com","Facebook: @TamarSarc","Twitter: @enstamarsarc"
                    ,"Evelyn Ngozi Onah  ","08068528819","ngozievelyn96@gmail.com"),

                SARCsSupport(8,"Jigawa SARC","Jigawa","Dutse General Hospital 10 Kiyawa Road, Dutse, Jigawa",
                    "09033035588, 08069444225","hopecenteryolaf","Facebook: @Jigawa Sexual Assault Referral Center ","Twitter: @JigawaCenter"
                    ,"Dr Abbas Yau Garba","09033035588","yauichd2013@gmail.com"),

                SARCsSupport(9,"Salama Centre 1","Kaduna","Gwamna Awan General Hospital, Kakuri, Kaduna",
                    "08092049888, 09029991140 07018160776, 09032063303   ","salamasarc2017@qmail.com","Facebook: @SaIama Sarc","Twitter: @SalamaSarc"
                    ,"Julianna Joseph","08033837025","julieneemalachy@gmail.com"),



                SARCsSupport(10,"Salama Centre 2","Kaduna","Yusuf Dantsoho Mernorial Hospital, Tundun Wada, Kaduna",
                    "09011578622, 08063968541, 08024440733, 07030896901","salamacenterlg@gmail.com","Facebook: @Salama Sarc","Twitter: @SalamaSarc"
                    ,"Sidikat Adegboye Bello","08092899741,08092877682","cdqah2@yahoo.com"),

                SARCsSupport(11,"Salama Centre 3","Kaduna","Gambo Sawaba General Hospital, Zaria, Kaduna",
                    "0809331481 , 08093314800, 08093314855, 08093314844","salamacentrezaria@gmail.com","Facebook: @Salama Sarc","Twitter: @SaIamaSarc"
                    ,"Amina Ladan","08065826024",""),

                SARCsSupport(11,"Salama Centre 4","Kaduna","Ibrahim Patrick Yakowa General Hospital, Kafanchan, Kaduna",
                    "09061503384, 09067528082, 09032488802","salamasarcKaf@gmail.com","Facebook: @Salama Sarc","Twitter: @SalamaSarc"
                    ,"Grace Abbin Yohanna","08035869981, 08148026802",""),

                SARCsSupport(11,"Waraka SARC","Kano","Murtala Muhammad Specialist Hospital, Kano",
                    "09028944933, 09030424123","warakasarckano@gmail.com","",""
                    ,"Dr Nasir Garko","08065340578","nasgak@gmail.com"),

                SARCsSupport(11,"Mirabel Centre","Lagos","Lagos State University Teaching Hospital (LASIJTH), Ikeja, Lagos",
                    "07013491769, 08187243468, 08155770000","sarc@pjnigeria.org  ","Instagram: @mirabelcentreng Facebook: @Mirabel Centre-Sexual Assault Referral Centre, Lagos","Twitter: @MirabecentreNG"
                    ,"Juliet Olumuyiwa-Rufai","07013491769","julietmyjewel@yahoo.com"),

                SARCsSupport(11,"Women at Risk Foundation (WARIF) Centre","Lagos","6 Turton Street, Off Thorbum Avenue, Yaba, Lagos",
                    "08092100009","nfo@warifng.org","Instagram: @warif_ng Facebook: @WARIFNG","Twitter:@warif_ng"
                    ,"Dr Aniekan Makanjuola","07060568196","bridget.makanjuola@warifng.org"),

                SARCsSupport(11,"The Cece Yara Child Advocacy Centre","Lagos","2A Akin Ogunmade Davies Close, Gbagada Phase 2, Lagos",
                    "08008008001, 07007007001","help@ceceyara.org  ","Instagram: @cece_yara Facebook: @The Cece Yara Foundation","Twitter. @cece_yara"
                    ,"Grace Ketefe,Bisi Ajayi-Kayode","09085692623, 08034083896, 09085692612","gketefe@ceceyara.org, oajayi-kayode@ceceyataprg")
            )
        )
    }
}
