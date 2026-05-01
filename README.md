# CollectorApp Android v3

مشروع Android Studio فعلي بكوتلن لتطبيق المحصل.

## الموجود في هذه النسخة
- تسجيل دخول فعلي عبر API
- حفظ الجلسة محليًا
- تنزيل المستحقات من API إلى Room
- عرض المشتركين والتفاصيل والفواتير
- إنشاء مسودات تحصيل مع توزيع على عدة فواتير
- رفع التحصيلات المعلقة إلى API
- تنزيل قرارات الإدارة وتحديث الحالة محليًا

## API المتوقع
هذا المشروع مصمم ليتكامل مع مشروع backend المرفق سابقًا:
- `POST /api/auth/login`
- `GET /api/mobile-sync/receivables`
- `POST /api/mobile-sync/upload-batch`
- `GET /api/mobile-sync/import-decisions?deviceCode=...&changedAfter=...`

## قبل التشغيل
1. افتح المشروع في Android Studio.
2. عدّل Base URL من شاشة الدخول.
3. تأكد أن الـ backend يعمل ويعيد JSON بالأسماء camelCase.
4. جرّب تسجيل الدخول ثم افتح شاشة المزامنة.

## ملاحظات
- هذه النسخة مبنية لتكون عملية ومنظمة، لكنها قد تحتاج تعديلات بسيطة حسب إصدار Android Studio أو شكل API النهائي.
- إذا كان السيرفر محليًا أثناء التطوير على المحاكي، استخدم غالبًا: `http://10.0.2.2:5099/`

## Full project files included
This package includes:
- root Gradle files
- app module Gradle file
- Gradle wrapper scripts (`gradlew`, `gradlew.bat`)
- wrapper configuration under `gradle/wrapper/`
- `local.properties.example`
- complete Android source and resources

Open the folder in Android Studio, copy `local.properties.example` to `local.properties`, set your Android SDK path, then sync the project.
