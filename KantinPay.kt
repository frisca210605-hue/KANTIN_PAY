// ==========================================
// TAHAP 3: IMPLEMENTASI ENKAPSULASI KOTLIN
// TEMA 1: KantinPay ITK
// ==========================================

class Mahasiswa(val idMahasiswa: String, val namaMahasiswa: String, private val pinRahasia: String, saldoAwal: Int) {
    // 1. DATA HIDING (20 Poin): Saldo dilindungi secara ketat agar tidak bisa menjadi minus
    var saldo: Int = saldoAwal
        private set // Hanya bisa dibaca dari luar, TIDAK BISA diubah sembarangan dengan '='

    // 2. CUSTOM SETTER & VALIDASI (20 Poin): Jalur resmi memotong saldo
    fun prosesPembayaran(totalHarga: Int, inputPin: String): Boolean {
        // Validasi Aturan Bisnis: PIN harus benar
        if (inputPin != pinRahasia) {
            println("❌ GAGAL: PIN yang dimasukkan SALAH!")
            return false
        }
        // Validasi Aturan Bisnis: Saldo harus mencukupi (tidak boleh minus)
        if (saldo < totalHarga) {
            println("❌ GAGAL: Saldo tidak mencukupi! (Saldo: Rp$saldo, Harga: Rp$totalHarga)")
            return false
        }
        
        // Jika semua syarat lolos, potong saldo
        saldo -= totalHarga
        return true
    }
}

class Menu(val namaMenu: String, val harga: Int, stokAwal: Int) {
    // 1. DATA HIDING: Stok makanan tidak boleh diubah sembarangan
    var stok: Int = stokAwal
        private set

    // Validasi Aturan Bisnis: Stok Menu > 0
    fun cekKetersediaan(): Boolean {
        if (stok <= 0) {
            println("❌ GAGAL: Stok menu '$namaMenu' sudah habis!")
            return false
        }
        return true
    }

    // Custom setter untuk mengurangi stok dengan aman
    fun kurangiStok() {
        if (stok > 0) stok--
    }
}

class StandMakanan(val namaStand: String) {
    // Fungsi untuk mengelola interaksi antar objek
    fun prosesTransaksi(mhs: Mahasiswa, menu: Menu, inputPin: String) {
        println("\n--- Memproses Transaksi di $namaStand ---")
        println("Aktor: ${mhs.namaMahasiswa} mencoba membeli ${menu.namaMenu}")

        // Mengeksekusi Logika If-Else sesuai Aturan Bisnis
        if (menu.cekKetersediaan()) {
            if (mhs.prosesPembayaran(menu.harga, inputPin)) {
                menu.kurangiStok()
                println("✅ SUKSES: Transaksi pembayaran berhasil!")
                println("   -> Saldo ${mhs.namaMahasiswa} terpotong Rp${menu.harga} (Sisa Saldo: Rp${mhs.saldo}).")
                println("   -> Sisa stok ${menu.namaMenu} saat ini: ${menu.stok} porsi.")
            } else {
                println("   -> Transaksi Dibatalkan oleh Sistem KantinPay.")
            }
        } else {
            println("   -> Transaksi Dibatalkan: Makanan tidak tersedia.")
        }
    }
}

// 3. SIMULASI DI MAIN FUNCTION (15 Poin)
fun main() {
    println("=== SIMULASI SISTEM KANTINPAY ITK ===\n")
    val stand = StandMakanan("Kantin Gedung G")

    // ---------------------------------------------------------
    // SKENARIO 1: GAGAL (Stok Habis)
    // ---------------------------------------------------------
    println(">> SIMULASI 1: GAGAL (STOK HABIS)")
    val mhs1 = Mahasiswa("04231038", "Hendra Firmansyah", "123456", 50000)
    val menu1 = Menu("Ayam Geprek", 15000, 0) // Sengaja diset stok 0
    stand.prosesTransaksi(mhs1, menu1, "123456")

    // ---------------------------------------------------------
    // SKENARIO 2: GAGAL (PIN Salah)
    // ---------------------------------------------------------
    println("\n>> SIMULASI 2: GAGAL (PIN SALAH)")
    val mhs2 = Mahasiswa("04231084", "Syamsul", "654321", 50000)
    val menu2 = Menu("Nasi Goreng", 15000, 5) 
    stand.prosesTransaksi(mhs2, menu2, "000000") // Input PIN yang salah

    // ---------------------------------------------------------
    // SKENARIO 3: GAGAL (Saldo Kurang)
    // ---------------------------------------------------------
    println("\n>> SIMULASI 3: GAGAL (SALDO KURANG)")
    val mhs3 = Mahasiswa("04231093", "Zaki", "111111", 10000) // Saldo hanya 10rb
    stand.prosesTransaksi(mhs3, menu2, "111111") // Harga nasi goreng 15rb

    // ---------------------------------------------------------
    // SKENARIO 4: SUKSES (Memenuhi semua syarat)
    // ---------------------------------------------------------
    println("\n>> SIMULASI 4: SUKSES (TRANSAKSI SAH)")
    val mhs4 = Mahasiswa("04231035", "Frisca Aulia", "222333", 100000) 
    val menu3 = Menu("Soto Ayam", 12000, 10) // Stok aman
    stand.prosesTransaksi(mhs4, menu3, "222333") // PIN benar, Saldo Cukup
}